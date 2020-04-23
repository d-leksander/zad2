package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BasketRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val userRepository: UserRepository)(implicit ec: ExecutionContext) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  import userRepository.UserTable
  private val product = TableQuery[UserTable]

  class BasketTable(tag: Tag) extends Table[Basket](tag, "basket") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def user = column[Int]("user")
    def user_fk = foreignKey("user_fk",user,product)(_.user_id)
    def * = (id, user) <> ((Basket.apply _).tupled, Basket.unapply)
  }
  private val basket = TableQuery[BasketTable]

  def create(user: Int): Future[Basket] = db.run {
    (basket.map(p => (p.user))
      returning basket.map(_.id)
      into {case ((user),id) => Basket(id,user)}
      ) += user
  }

  def list(): Future[Seq[Basket]] = db.run {
    basket.result
  }
  def delete(id: Int): Future[Unit] = db.run(basket.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, new_Basket: Basket): Future[Unit] = {
    val update_Basket: Basket = new_Basket.copy(id)
    db.run(basket.filter(_.id === id).update(update_Basket)).map(_ => ())
  }

}