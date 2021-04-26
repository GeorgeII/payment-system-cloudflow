/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package paymentdata

import scala.annotation.switch

case class InvalidTransfer(var transfer: String, var reason: String) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this("", "")
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        transfer
      }.asInstanceOf[AnyRef]
      case 1 => {
        reason
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.transfer = {
        value.toString
      }.asInstanceOf[String]
      case 1 => this.reason = {
        value.toString
      }.asInstanceOf[String]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = InvalidTransfer.SCHEMA$
}

object InvalidTransfer {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"InvalidTransfer\",\"namespace\":\"paymentdata\",\"fields\":[{\"name\":\"transfer\",\"type\":\"string\"},{\"name\":\"reason\",\"type\":\"string\"}]}")
}