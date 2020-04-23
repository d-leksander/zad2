package models

import play.api.libs.json.Json

case class Product(product_id: Long, name: String, description: String, price: Long, category: Int)

object Product {
  implicit val productFormat = Json.format[Product]
}