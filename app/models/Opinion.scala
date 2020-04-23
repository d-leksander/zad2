package models

import play.api.libs.json._

case class Opinion(id: Int, product_id: Int, text: String)

object Opinion {
  implicit val opinionFormat = Json.format[Opinion]
}