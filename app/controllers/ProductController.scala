package controllers

import javax.inject._
import models.{Category, CategoryRepository, Product, ProductRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ProductController @Inject()(productsRepository: ProductRepository, categoryRepository: CategoryRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val productForm: Form[CreateProductForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "price" -> longNumber,
      "category" -> number,
    )(CreateProductForm.apply)(CreateProductForm.unapply)
  }

  val updateProductForm: Form[UpdateProductForm] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "price" -> longNumber,
      "category" -> number,
    )(UpdateProductForm.apply)(UpdateProductForm.unapply)
  }

  def index = Action {
    Ok(views.html.index("APP IS READY."))
  }

  def getProducts: Action[AnyContent] = Action.async { implicit request =>
    val produkty = productsRepository.list()
    produkty.map( products => Ok(views.html.products(products)))
  }

  def getProduct(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val produkt = productsRepository.getByIdOption(id)
    produkt.map(product => product match {
      case Some(p) => Ok(views.html.product(p))
      case None => Redirect(routes.HomeController.getProducts())
    })
  }

  def delete(id: Long): Action[AnyContent] = Action {
    productsRepository.delete(id)
    Redirect("/products")
  }

  def updateProduct(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    var categ:Seq[Category] = Seq[Category]()
    val categories = categoryRepository.list().onComplete{
      case Success(cat) => categ = cat
      case Failure(_) => print("fail")
    }

    val produkt = productsRepository.getById(id)
    produkt.map(product => {
      val prodForm = updateProductForm.fill(UpdateProductForm(product.product_id, product.name, product.description, product.price, product.category))

      Ok(views.html.productupdate(prodForm, categ))
    })
  }

  def updateProductHandle = Action.async { implicit request =>
    var categ:Seq[Category] = Seq[Category]()
    val categories = categoryRepository.list().onComplete{
      case Success(cat) => categ = cat
      case Failure(_) => print("fail")
    }

    updateProductForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.productupdate(errorForm, categ))
        )
      },
      product => {
        productsRepository.update(product.id, Product(product.id, product.name, product.description, product.price, product.category)).map { _ =>
          Redirect(routes.ProductController.updateProduct(product.id)).flashing("success" -> "product updated")
        }
      }
    )

  }


  def addProduct: Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val categories = categoryRepository.list()
    categories.map (cat => Ok(views.html.productadd(productForm, cat)))
  }

  def addProductHandle = Action.async { implicit request =>
    var categ:Seq[Category] = Seq[Category]()
    val categories = categoryRepository.list().onComplete{
      case Success(cat) => categ = cat
      case Failure(_) => print("fail")
    }

    productForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.productadd(errorForm, categ))
        )
      },
      product => {
        productsRepository.create(product.name, product.description, product.category).map { _ =>
          Redirect(routes.HomeController.addProduct()).flashing("success" -> "product.created")
        }
      }
    )

  }

}

case class CreateProductForm(name: String, description: String, price: Long, category: Int)
case class UpdateProductForm(id: Long, name: String, description: String, price: Long, category: Int)