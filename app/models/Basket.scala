package models

import play.api.libs.json._

case class Basket(id: Int, user: Int)

object Basket {
  implicit val basketFormat = Json.format[Basket]
}