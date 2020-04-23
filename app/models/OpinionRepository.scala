package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{Future, ExecutionContext}

@Singleton
class OpinionRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, productRepository: ProductRepository)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class OpinionTable(tag: Tag) extends Table[Opinion](tag, "opinion") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def product_id = column[Int]("product_id")
    def product_id_fk = foreignKey("product_id_fk", product_id, product)(_.product_id)
    def text = column[String]("text")
    def * = (id, product_id, text) <> ((Opinion.apply _).tupled, Opinion.unapply)
  }

  import productRepository.ProductTable
  private val opinion = TableQuery[OpinionTable]
  private val product = TableQuery[ProductTable]

  def create(product_id: Int, text: String): Future[Opinion] = db.run {
    (opinion.map(r => (r.product_id, r.text))
      returning opinion.map(_.id)
      into { case ((product_id, text), id) => Opinion(id, product_id,text) }
      ) += ((product_id, text))
  }

  def list(): Future[Seq[Opinion]] = db.run {
    opinion.result
  }
  def getByProductId(productId: Int): Future[Seq[Opinion]] = {
    db.run {
      opinion.filter(_.product_id === productId).result
    }
  }
  def delete(id: Int): Future[Unit] = db.run(opinion.filter(_.id === id).delete).map(_ => ())
  def update(value: Opinion): Future[Int] = db.run {
    opinion.insertOrUpdate(value)
  }
}