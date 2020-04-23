package models

import java.sql.Timestamp

import play.api.libs.json._

case class User (user_id: Int, name:String, email_address: String, password: String, address: String, phone_number: String)

object User {
  implicit val userFormat = Json.format[User]
}

