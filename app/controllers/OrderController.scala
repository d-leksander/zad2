package controllers
import javax.inject._
import play.api._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext
import models.{Order, OrderRepository}
import models.{Delivery, DeliveryRepository}

class OrderController @Inject()(orderRepository: OrderRepository, deliveryRepository: DeliveryRepository, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def addOrder = Action {
    Ok("AddOrder")

    }

  def orderList() = Action.async { implicit request: Request[AnyContent] =>
    val ordlist = deliveryRepository.list()
    ordlist.map(ordlist => {
      Ok(s"Orderer: $ordlist")
    })
  }

  def getOrderById(id: Int) = Action.async { implicit request: Request[AnyContent] =>
    val ord = orderRepository.getById(id.toInt)
    ord.map( ord => Ok(s"Orders $ord"))
  }
  def deliveriesList() = Action.async { implicit request: Request[AnyContent] =>
    val deliver = deliveryRepository.list()
    deliver.map(delivery => {
      Ok(s"Deliveries: $deliver")
    })
  }
  def getDeliveryById(id: Int) = Action.async { implicit request: Request[AnyContent] =>
    val deliverid = orderRepository.getById(id.toInt)
    deliverid.map( deliverid => Ok(s"Orders $deliverid"))
  }



}
