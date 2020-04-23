package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }


@Singleton
class AdminRepository@Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  protected val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class AdminTable(tag: Tag) extends Table[Admin](tag, "admin"){

    def admin_id = column[Int]("admin_id", O.PrimaryKey, O.AutoInc)
    def adminName = column[String]("adminName")
    def adminPassword = column[String]("adminPassword")

    def * = (admin_id, adminName, adminPassword)  <> ((Admin.apply _).tupled, Admin.unapply)

  }

  private val admin = TableQuery[AdminTable]

  def create(adminNname: String, adminPassword: String): Future[Admin] = db.run {
    (admin.map(a => (a.adminName, a.adminPassword))
      returning admin.map(_.admin_id)
      into { case ((adminName, adminPassword), admin_id) => Admin(admin_id,adminName,adminPassword)}
      ) += (adminNname, adminPassword)
  }

  def list(): Future[Seq[Admin]] = db.run {
    admin.result
  }
  def findById(id: Int): Future[Option[Admin]] = db.run(admin.filter(_.admin_id === id).result.headOption)
  def delete(id: Int): Future[Unit] = db.run(admin.filter(_.admin_id === id).delete).map(_ => ())

  def update(value: Admin): Future[Int] = db.run {
    admin.insertOrUpdate(value)
  }
}