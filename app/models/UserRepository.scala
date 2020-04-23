package models

import java.sql.Timestamp

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class UserTable(tag: Tag) extends Table[User](tag, "user") {

    def user_id = column[Int]("user_id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def email_address = column[String]("email_address")
    def password = column[String]("password")
    def address = column[String]("address")
    def phone_number = column[String]("phone_number")



    def * = (user_id, name, email_address, password, address, phone_number) <> ((User.apply _).tupled, User.unapply)
  }

  val user = TableQuery[UserTable]

  def create(name:String, email_address: String, password: String, address: String, phone_number: String): Future[User] = db.run {

    (user.map(u => (u.name, u.email_address, u.password, u.address, u.phone_number))
      returning user.map(_.user_id)
      into { case ((name, email_address, password, address, phone_number), user_id)
    => User(user_id, name, email_address, password, address, phone_number) }
      ) += (name, email_address, password, address, phone_number)
  }

  def list(): Future[Seq[User]] = db.run {
    user.result
  }
  def delete(user_id: Int): Future[Unit] = db.run(user.filter(_.user_id === user_id).delete).map(_ => ())

  def update(user_id: Int, newUser: User): Future[Unit] = {
    val updateUser: User = newUser.copy(user_id)
    db.run(user.filter(_.user_id === user_id).update(updateUser)).map(_ => ())
  }

}