package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CategoryRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {



  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class CategoryTable(tag: Tag) extends Table[Category](tag, "category") {

    def category_id = column[Int]("category_id", O.PrimaryKey, O.AutoInc)
    def category_name = column[String]("category_name")


    def * = (category_id, category_name) <> ((Category.apply _).tupled, Category.unapply)
  }

  val category = TableQuery[CategoryTable]

  def create(category_name: String): Future[Category] = db.run {
    (category.map(c => (c.category_name))
      returning category.map(_.category_id)
      into { case ((category_name), category_id) => Category(category_id, category_name) }
      ) += (category_name)
  }

  def list(): Future[Seq[Category]] = db.run {
    category.result
  }

  def getCategoryById(id: Int): Future[Seq[Category]] = db.run {
    category.filter(_.category_id === id).result
  }

}