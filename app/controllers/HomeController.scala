package controllers

import javax.inject._
import models.ProductRepository
import play.api.mvc._

import scala.concurrent.ExecutionContext
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(productRepository: ProductRepository, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index("APP IS READY"))
  }
  def getProducts: Action[AnyContent] = Action.async { implicit request =>
    val products = productRepository.list()
    products.map( products => Ok(views.html.products(products)))
  }

}