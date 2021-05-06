package paymentdata

import akka.stream.scaladsl.{Flow, Source}
import cloudflow.akkastream.scaladsl.RunnableGraphStreamletLogic
import cloudflow.akkastream.AkkaStreamlet
import cloudflow.streamlets.StreamletShape
import cloudflow.streamlets.avro.AvroOutlet
import paymentdata.utils.FileReader.{getAllFilesFromFolder, readLinesInFile}

import java.io.File
import scala.concurrent.duration.DurationInt

class FilePaymentsIngress extends AkkaStreamlet {

  val out   = AvroOutlet[Transfer]("out")
  def shape = StreamletShape.withOutlets(out)

  override def createLogic = new RunnableGraphStreamletLogic() {
    override def runnableGraph = {
      val sourceFiles   = Source(getAllFilesFromFolder)
      val linesFromFile = Flow[File].map(file => readLinesInFile(file))
      val flattening    = Flow[Vector[String]].mapConcat(identity)
      val wrapping      = Flow[String].map(payment => Transfer(payment))

      sourceFiles
        .via(linesFromFile)
        .async
        .via(flattening)
        .async
        .via(wrapping)
        .async
        // remove this line to get rid of throttling
        .throttle(3000, 1.second)
        .to(plainSink(out))
    }
  }
}
