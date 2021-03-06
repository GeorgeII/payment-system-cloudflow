package paymentdata

import cloudflow.flink.{ FlinkStreamlet, FlinkStreamletLogic }
import cloudflow.streamlets.StreamletShape
import cloudflow.streamlets.avro.{ AvroInlet, AvroOutlet }
import org.apache.flink.api.common.state.{ MapState, MapStateDescriptor }
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction
import org.apache.flink.streaming.api.scala.{ createTypeInformation, DataStream }
import org.apache.flink.util.Collector

case class SuccessfulPayment(message: String)

class PaymentProcessingStreamlet extends FlinkStreamlet {

  @transient val inParticipants = AvroInlet[BankAccount]("accounts")
  @transient val inPayments     = AvroInlet[ParsedPayment]("payments")
  @transient val outInvalid     = AvroOutlet[InvalidTransfer]("invalid")

  override def shape = StreamletShape.withInlets(inParticipants, inPayments).withOutlets(outInvalid)

  override def createLogic() = new FlinkStreamletLogic() {
    override def buildExecutionGraph(): Unit = {
      val keyedAccounts: DataStream[BankAccount] =
        readStream(inParticipants)
          .keyBy(_.accountId)

      // This keyBy is a must because later on we connect it with `keyedAccounts`.
      // Technically, it does nothing and we need it only for compatibility with CoFlatMap because otherwise an error
      // will be thrown during runtime.
      val keyedPayments: DataStream[ParsedPayment] =
        readStream(inPayments)
          .keyBy(_.fromParticipant)

      val processedPayments: DataStream[InvalidTransfer] =
        keyedAccounts
          .connect(keyedPayments)
          .flatMap(new EnrichmentFunction)
          .filter(_.isLeft)
          .map(_.left.get)

      writeStream(outInvalid, processedPayments)
    }
  }


  class EnrichmentFunction extends RichCoFlatMapFunction[BankAccount, ParsedPayment, Either[InvalidTransfer, SuccessfulPayment]] {

    type Balance   = Long
    type AccountId = String

    private lazy val accountsState: MapState[AccountId, Balance] =
      getRuntimeContext
        .getMapState(
          new MapStateDescriptor[AccountId, Balance]("balance", classOf[AccountId], classOf[Balance])
        )

    /**
     * Emits zero elements.
     * @param out though, it has a parameter type, it does not contain anything.
     */
    override def flatMap1(account: BankAccount, out: Collector[Either[InvalidTransfer, SuccessfulPayment]]): Unit =
      // if an account was already initialized - we skip and do nothing. So, only the first init is legal.
      // (the generator of accounts might send multiple initial balances for the same account.
      // This is the anomaly we are coping with here).
      if (!accountsState.contains(account.accountId)) {
        accountsState.put(account.accountId, account.balance)
      }

    override def flatMap2(payment: ParsedPayment, out: Collector[Either[InvalidTransfer, SuccessfulPayment]]): Unit = {
      val from  = payment.fromParticipant
      val to    = payment.toParticipant
      val value = payment.value

      // if both participants are initialized
      if (accountsState.contains(from) &&
          accountsState.contains(to)) {

        val balanceFrom = accountsState.get(from)
        val balanceTo   = accountsState.get(to)

        if (balanceFrom >= value) {
          accountsState.put(from, balanceFrom - value)
          accountsState.put(to, balanceTo + value)
          out.collect(Right(SuccessfulPayment(s"Payment $payment was successfully processed.")))
        } else {
          out.collect(Left(InvalidTransfer(payment.toString, "InsufficientBalance")))
        }
      } else {
        out.collect(Left(InvalidTransfer(payment.toString, "AccountNotFound")))
      }
    }
  }
}
