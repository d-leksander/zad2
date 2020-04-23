package models

import play.api.libs.json._

case class Types(id: Int, product_id: Int, type_field: String)

object Types {
  implicit val typesFormat = Json.format[Types]
}
