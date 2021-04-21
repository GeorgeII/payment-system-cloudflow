package paymentdata.utils

import scala.util.Random

object PaymentUtils {

  private val rnd = new Random

  /**
   * @return random Long in range [10_000, 110_000).
   */
  def generateBalance(): Long = {
    rnd.nextLong(100_000) + 10_000
  }

  def isValid(payment: String): Boolean = {
    val namePattern    = "[a-zA-Z0-9]+".r
    val valuePattern   = "[0-9]+".r
    val paymentPattern = s"$namePattern->$namePattern:$valuePattern".r

    payment.matches(paymentPattern.toString)
  }

  /**
   * Important: the payment should be valid in terms of syntax.
   * @param payment participant1->participant2:value type of string
   * @return (participant1, participant2, value)
   */
  def extractParticipantsAndValue(payment: String): (String, String, String) = {
    // Decided to try an easier solution with Regex. Haven't tested it, so a few bugs might occur.
    // In the case, use commented code below.
    val namePattern    = "[a-zA-Z0-9]+".r
    val valuePattern   = "[0-9]+".r
    val paymentPattern = s"$namePattern->$namePattern:$valuePattern".r

    val paymentPattern(firstParticipant, secondParticipant, value) = payment

    (firstParticipant, secondParticipant, value)

    //    val endOfFirstParticipant = payment.indexOf("-")
    //    val firstParticipant = payment.substring(0, endOfFirstParticipant)
    //
    //    val startOfSecondParticipant = payment.indexOf(">")
    //    val endOfSecondParticipant = payment.indexOf(":")
    //    val secondParticipant = payment.substring(
    //      startOfSecondParticipant + 1,
    //      endOfSecondParticipant
    //    )
    //
    //    val value = payment.substring(endOfSecondParticipant + 1)
  }
}
