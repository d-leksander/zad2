package models


import play.api.libs.json._

case class Admin(admin_id: Int, adminName: String, adminPassword: String)

object Admin {
  implicit val adminFormat = Json.format[Admin]
}
