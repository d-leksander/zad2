package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val userRepository: UserRepository)(implicit ec: ExecutionContext) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  import userRepository.UserTable

  class OrderTable(tag: Tag) extends Table[Order](tag, "order") {

    def order_id = column[Int]("pd_id", O.PrimaryKey, O.AutoInc)
    def user_id = column[Int]("user_id")
    def status = column[String]("status")
    def date = column[String]("date")
    def payment = column[Float]("price_total")
    def user_fk = foreignKey("order_fkey", user_id, us)(_.user_id)


    def * = (order_id, user_id, status, date, payment) <> ((Order.apply _).tupled, Order.unapply)
  }

  val order = TableQuery[OrderTable]
  private val us = TableQuery[UserTable]

  def create(user_id: Int, status: String, date: String, payment: Float): Future[Order] = db.run {
    (order.map(o => (o.user_id, o.date, o.status, o.payment))
      returning order.map(_.order_id)
      into { case ((user_id, status, date, payment), order_id) => Order(order_id, user_id, status, date, payment) }
      ) += (user_id, status, date, payment)
  }

  def list(): Future[Seq[Order]] = db.run {
    order.result
  }
  def getById(id: Int): Future[Seq[Order]] = db.run {
    order.filter(_.order_id === id).result
  }
  def getByUserId(user_id: Int): Future[Seq[Order]] = db.run {
    order.filter(_.user_id === user_id).result
  }
}
