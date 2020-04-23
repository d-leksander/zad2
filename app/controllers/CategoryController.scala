package controllers
import javax.inject._
import play.api._
import play.api.mvc._

import javax.inject.Inject
import play.api.mvc.{AbstractController, ControllerComponents}
import models.{Category, CategoryRepository}
import scala.concurrent.{ExecutionContext, Future}

class CategoryController @Inject()(categoryRepository: CategoryRepository, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {


  def getAll = {
    Action.async { implicit request: Request[AnyContent] =>
      val kat = categoryRepository.list()
      kat.map(kat => {
        Ok(s"Kat $kat")
      })

    }
  }
    def showCategoryId(category: String): Action[AnyContent] = {
      Action.async { implicit request: Request[AnyContent] =>
        categoryRepository.getCategoryById(category.toInt).map {
          category => Ok("Show category action")
        }
      }
    }
}





