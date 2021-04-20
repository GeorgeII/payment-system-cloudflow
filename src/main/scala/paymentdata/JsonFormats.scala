package paymentdata

import spray.json.{ DefaultJsonProtocol, RootJsonFormat }

object TransferJsonSupport extends DefaultJsonProtocol {
  implicit val transferFormat: RootJsonFormat[Transfer] = jsonFormat1(Transfer.apply)
}
