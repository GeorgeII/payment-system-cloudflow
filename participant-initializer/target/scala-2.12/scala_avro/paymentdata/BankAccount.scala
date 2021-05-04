/** MACHINE-GENERATED FROM AVRO SCHEMA. DO NOT EDIT DIRECTLY */
package paymentdata

import scala.annotation.switch

case class BankAccount(var accountId: String, var balance: Long) extends org.apache.avro.specific.SpecificRecordBase {
  def this() = this("", 0L)
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case 0 => {
        accountId
      }.asInstanceOf[AnyRef]
      case 1 => {
        balance
      }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case 0 => this.accountId = {
        value.toString
      }.asInstanceOf[String]
      case 1 => this.balance = {
        value
      }.asInstanceOf[Long]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  def getSchema: org.apache.avro.Schema = BankAccount.SCHEMA$
}

object BankAccount {
  val SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"BankAccount\",\"namespace\":\"paymentdata\",\"fields\":[{\"name\":\"accountId\",\"type\":\"string\"},{\"name\":\"balance\",\"type\":\"long\"}]}")
}