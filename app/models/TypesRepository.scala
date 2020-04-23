package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TypesRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, productRepository: ProductRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class TypesTable(tag: Tag) extends Table[Types](tag, "types") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def product_id = column[Int]("product_id")
    def type_field = column[String]("type_field")
    def product_id_fk = foreignKey("product_id_fk", product_id, product)(_.product_id)

    def * = (id, product_id, type_field) <> ((Types.apply _).tupled, Types.unapply)
  }

  import productRepository.ProductTable

  private val type_field = TableQuery[TypesTable]
  private val product = TableQuery[ProductTable]

  def create(product_id: Int, type_fi: String): Future[Types] = db.run {
    (type_field.map(t => (t.product_id, t.type_field))
      returning type_field.map(_.id)
      into { case ((product_id, type_fi), id) => Types(id, product_id, type_fi) }
      ) += ((product_id, type_fi))
  }

  def list(): Future[Seq[Types]] = db.run {
    type_field.result
  }
}