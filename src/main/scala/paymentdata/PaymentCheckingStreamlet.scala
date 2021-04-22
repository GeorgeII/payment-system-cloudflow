package paymentdata

import cloudflow.flink.{ FlinkStreamlet, FlinkStreamletLogic }
import cloudflow.streamlets.StreamletShape
import cloudflow.streamlets.avro.{ AvroInlet, AvroOutlet }
import org.apache.flink.streaming.api.scala.{ createTypeInformation, DataStream }
import paymentdata.utils.PaymentUtils.{ extractParticipantsAndValue, isValid }

class PaymentCheckingStreamlet extends FlinkStreamlet {

  @transient val in         = AvroInlet[Transfer]("in")
  @transient val outInvalid = AvroOutlet[InvalidTransfer]("invalid")
  @transient val outPayment = AvroOutlet[ParsedPayment]("out  ")

  def shape =
    StreamletShape
      .withInlets(in)
      .withOutlets(outInvalid, outPayment)

  override def createLogic() = new FlinkStreamletLogic() {
    override def buildExecutionGraph(): Unit = {
      val transfers: DataStream[Transfer] = readStream(in)

      val validPayments =
        transfers
          .filter(transfer => isValid(transfer.data))
          .map(transfer =>
            extractParticipantsAndValue(transfer.data) match {
              case (p1, p2, value) => ParsedPayment(p1, p2, value)
            }
          )

      val invalidTransfers =
        transfers
          .filter(transfer => !isValid(transfer.data))
          .map(transfer => InvalidTransfer(transfer.data, "IncorrectAttributes"))

      writeStream(outPayment, validPayments)
      writeStream(outInvalid, invalidTransfers)
    }
  }
}
