/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package paymentdata

import scala.annotation.switch

case class ParsedPayment(var fromParticipant: String, var toParticipant: String, var value: Long) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this("", "", 0L)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        fromParticipant
      }.asInstanceOf[AnyRef]
      case 1 => {
        toParticipant
      }.asInstanceOf[AnyRef]
      case 2 => {
        value
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.fromParticipant = {
        value.toString
      }.asInstanceOf[String]
      case 1 => this.toParticipant = {
        value.toString
      }.asInstanceOf[String]
      case 2 => this.value = {
        value
      }.asInstanceOf[Long]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = ParsedPayment.SCHEMA$
}

object ParsedPayment {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ParsedPayment\",\"namespace\":\"paymentdata\",\"fields\":[{\"name\":\"fromParticipant\",\"type\":\"string\"},{\"name\":\"toParticipant\",\"type\":\"string\"},{\"name\":\"value\",\"type\":\"long\"}]}")
}