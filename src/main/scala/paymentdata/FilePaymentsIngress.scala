package paymentdata

import akka.stream.scaladsl.{ Flow, Source }
import cloudflow.akkastream.scaladsl.RunnableGraphStreamletLogic
import cloudflow.akkastream.AkkaStreamlet
import cloudflow.streamlets.StreamletShape
import cloudflow.streamlets.avro.AvroOutlet
import com.typesafe.config.ConfigFactory

import java.io.File
import scala.io.{ Source => ScalaFileSource }

class FilePaymentsIngress extends AkkaStreamlet {

  val out   = AvroOutlet[Transfer]("out")
  def shape = StreamletShape.withOutlets(out)

  private val config      = ConfigFactory.load("application.conf")
  private val configField = "payments-folder"

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
        .to(plainSink(out))
    }

    def getAllFilesFromFolder: Vector[File] = {
      val filesFolder = new File(config.getString(configField))

      if (filesFolder.exists && filesFolder.isDirectory) {
        filesFolder.listFiles.filter(_.isFile).toVector
      } else {
        Vector[File]()
      }
    }

    def readLinesInFile(file: File): Vector[String] = {
      val bufferedFileReader = ScalaFileSource.fromFile(file)
      val lines              = bufferedFileReader.getLines.toVector
      bufferedFileReader.close()

      lines
    }
  }
}
