package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DeliveryRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class DeliveryTable(tag: Tag) extends Table[Delivery](tag, "delivery") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def value = column[Int]("value")
    def status = column[Int]("status")
    def * = (id, name, value, status) <> ((Delivery.apply _).tupled, Delivery.unapply)
  }

  val delivery = TableQuery[DeliveryTable]

  def create(name: String, value: Int, status: Int): Future[Delivery] = db.run {
    (delivery.map(d => (d.name, d.value, d.status))
      returning delivery.map(_.id)
      into {case((name, value, status), id) => Delivery(id, name, value, status)}
      ) += (name, value, status)
  }

  def list(): Future[Seq[Delivery]] = db.run {
    delivery.result
  }

  def getById(id: Int): Future[Delivery] = db.run {
    delivery.filter(_.id === id).result.head
  }
}