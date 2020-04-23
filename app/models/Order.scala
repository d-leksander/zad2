package models

import play.api.libs.json._


case class Order(order_id: Int, user_id:Int, status: String, date: String, payment: Float)

object Order {
  implicit val orderFormat = Json.format[Order]
}