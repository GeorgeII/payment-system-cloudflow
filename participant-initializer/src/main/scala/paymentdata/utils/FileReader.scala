package paymentdata.utils

import com.typesafe.config.ConfigFactory

import java.io.File
import scala.io.Source

object FileReader {

  private val config      = ConfigFactory.load("application.conf")
  private val configField = "payments-folder"

  def getAllFilesFromFolder: Vector[File] = {
    val filesFolder = new File(config.getString(configField))

    if (filesFolder.exists && filesFolder.isDirectory) {
      filesFolder.listFiles.filter(_.isFile).toVector
    } else {
      Vector[File]()
    }
  }

  def readLinesInFile(file: File): Vector[String] = {
    val bufferedFileReader = Source.fromFile(file)
    val lines              = bufferedFileReader.getLines.toVector
    bufferedFileReader.close()

    lines
  }
}
