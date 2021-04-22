package paymentdata

import akka.stream.scaladsl.Sink
import cloudflow.akkastream.scaladsl.RunnableGraphStreamletLogic
import cloudflow.akkastream.AkkaStreamlet
import cloudflow.streamlets.StreamletShape
import cloudflow.streamlets.avro.AvroInlet

class PaymentLoggingEgress extends AkkaStreamlet {

  val in    = AvroInlet[InvalidTransfer]("validation")
  def shape = StreamletShape.withInlets(in)

  override def createLogic() = new RunnableGraphStreamletLogic() {
    override def runnableGraph() = {
      plainSource(in)
        .to(Sink.foreach {
          case InvalidTransfer(data, reason) => log.info(s"$reason [$data]")
//          TODO: case InsufficientBalance(data, ?)  => log.warn(s"")
        })
    }
  }

}
