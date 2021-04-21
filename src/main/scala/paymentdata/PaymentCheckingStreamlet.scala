package paymentdata

import cloudflow.flink.FlinkStreamlet
import cloudflow.streamlets.StreamletShape
import cloudflow.streamlets.avro.{AvroInlet, AvroOutlet}

class PaymentCheckingStreamlet extends FlinkStreamlet {

  @transient val inTransfers = AvroInlet[Transfer]("transfer-data")
  @transient val inAccounts  = AvroInlet[BankAccount]("accounts")
  @transient val outInvalid  = AvroOutlet[ParsedPayment]("validation")
  @transient val outPayment  = AvroOutlet[ParsedPayment]("payments")

  def shape = StreamletShape
    .withInlets(inTransfers, inAccounts)
    .withOutlets(outInvalid, outPayment)

  override def createLogic() = ???

}
