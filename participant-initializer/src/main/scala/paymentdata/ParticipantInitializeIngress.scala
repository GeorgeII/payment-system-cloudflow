package paymentdata

import akka.stream.scaladsl.{ Flow, Source }
import cloudflow.akkastream.scaladsl.RunnableGraphStreamletLogic
import cloudflow.akkastream.AkkaStreamlet
import cloudflow.streamlets.StreamletShape
import cloudflow.streamlets.avro.AvroOutlet
import paymentdata.utils.FileReader.{ getAllFilesFromFolder, readLinesInFile }
import paymentdata.utils.PaymentUtils.{ extractParticipantsAndValue, generateBalance, isValid }

import java.io.File

/**
 * Instead of receiving JSON data via REST requests, this streamlet randomly generates balances
 * based on existing accounts and passes on to the next streamlet.
 * Unfortunately, work with REST is omitted.
 */
class ParticipantInitializeIngress extends AkkaStreamlet {

  val out   = AvroOutlet[BankAccount]("out")
  def shape = StreamletShape.withOutlets(out)

  override def createLogic = new RunnableGraphStreamletLogic() {
    override def runnableGraph = {
      val sourceFiles         = Source(getAllFilesFromFolder)
      val linesFromFile       = Flow[File].map(readLinesInFile)
      val flatFiles           = Flow[Vector[String]].mapConcat(identity)
      val filterValidAccounts = Flow[String].filter(isValid)
      val extractingAccounts  = Flow[String].map(extractParticipantsAndValue)
      val removeValue         = Flow[(String, String, Long)].map { case (p1, p2, v) => List(p1, p2) }
      val flatParticipants    = Flow[List[String]].mapConcat(identity)
      val generateBankAccount = Flow[String].map(account => BankAccount(account, generateBalance()))

      sourceFiles
        .via(linesFromFile)
        .via(flatFiles)
        .via(filterValidAccounts)
        .async
        .via(extractingAccounts)
        .async
        .via(removeValue)
        .async
        .via(flatParticipants)
        .via(generateBankAccount)
        .async
        .to(plainSink(out))
    }
  }
}
