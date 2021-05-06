package paymentdata

import akka.stream.scaladsl.Sink
import cloudflow.akkastream.scaladsl.RunnableGraphStreamletLogic
import cloudflow.akkastream.AkkaStreamlet
import cloudflow.streamlets.StreamletShape
import cloudflow.streamlets.avro.AvroInlet

class PaymentLoggingEgress extends AkkaStreamlet {

  val in    = AvroInlet[InvalidTransfer]("in")
  def shape = StreamletShape.withInlets(in)

  override def createLogic() = new RunnableGraphStreamletLogic() {
    override def runnableGraph() =
      plainSource(in)
        .to(Sink.foreach {
          case InvalidTransfer(data, "IncorrectAttributes") => log.info(s"Incorrect attributes: $data")
          case InvalidTransfer(data, "InsufficientBalance") =>
            log.warn(s"Insufficient balance: $data. The payment was rejected")
          case InvalidTransfer(data, "AccountNotFound")     =>
            log.warn(s"One of participants (or both) has not been initialized yet: $data. The payment was rejected")
        })
  }

}
