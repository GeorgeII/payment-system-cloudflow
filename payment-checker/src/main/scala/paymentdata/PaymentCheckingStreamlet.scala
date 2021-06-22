package paymentdata

import cloudflow.flink.{ FlinkStreamlet, FlinkStreamletLogic }
import cloudflow.streamlets.StreamletShape
import cloudflow.streamlets.avro.{ AvroInlet, AvroOutlet }
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.{ createTypeInformation, DataStream, OutputTag }
import org.apache.flink.util.Collector
import paymentdata.utils.PaymentUtils.{ extractParticipantsAndValue, isValid }

class PaymentCheckingStreamlet extends FlinkStreamlet {

  @transient val in         = AvroInlet[Transfer]("in")
  @transient val outInvalid = AvroOutlet[InvalidTransfer]("invalid")
  @transient val outPayment = AvroOutlet[ParsedPayment]("out")

  def shape =
    StreamletShape
      .withInlets(in)
      .withOutlets(outInvalid, outPayment)

  override def createLogic() = new FlinkStreamletLogic() {
    override def buildExecutionGraph(): Unit = {
      val transfers: DataStream[Transfer] = readStream(in)

      val invalidTransfersOutputTag = OutputTag[InvalidTransfer]("invalid")

      val validPayments =
        transfers
          .process(new ProcessFunction[Transfer, ParsedPayment] {
            override def processElement(transfer: Transfer,
                                        ctx: ProcessFunction[Transfer, ParsedPayment]#Context,
                                        out: Collector[ParsedPayment]): Unit = {
              if (isValid(transfer.data)) {
                val parsedPayment = extractParticipantsAndValue(transfer.data) match {
                  case (p1, p2, value) => ParsedPayment(p1, p2, value)
                }

                out.collect(parsedPayment)
              } else {
                ctx.output(invalidTransfersOutputTag, InvalidTransfer(transfer.data, "IncorrectAttributes"))
              }
            }
          })

      val invalidTransfers = validPayments.getSideOutput(invalidTransfersOutputTag)

      writeStream(outPayment, validPayments)
      writeStream(outInvalid, invalidTransfers)
    }
  }
}
