package controllers
import javax.inject._
import play.api._
import play.api.mvc._
import models.{Basket, BasketRepository}

import scala.concurrent.ExecutionContext


class BasketController @Inject()(basketRepository: BasketRepository, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getBasket = Action.async { implicit request: Request[AnyContent] =>
    val basket = basketRepository.list()
    basket.map(basket =>
      Ok(s"Basketlist: $basket")
    )

  }
  def addToBasket(id: String) = Action { implicit request: Request[AnyContent] =>
    Ok(s"Add to basket: $id")
  }
  def updateBasket = Action {
    Ok(views.html.index("This is update action for Basket"))
  }
  def deleteBasket = Action {
    Ok(views.html.index("This is delete action for Basket"))
  }


}
