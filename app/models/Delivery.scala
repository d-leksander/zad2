package models

import play.api.libs.json._

case class Delivery(id: Int, name: String, value: Int, status: Int)

object Delivery {
  implicit val deliveryFormat = Json.format[Delivery]
}