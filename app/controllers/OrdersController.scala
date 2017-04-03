package controllers

import javax.inject.Inject

import model.Order._
import model.{Order, OrderSummary}
import play.api.libs.json.Json
import play.api.mvc.{Action, BodyParsers, Controller}
import services.OrdersService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Properties

class OrdersController @Inject() (ordersService: OrdersService) extends Controller {

  def registerOrder = Action.async(BodyParsers.parse.json) { request =>
    request.body.validate[Order].fold(
      errors => Future(InternalServerError(errors.mkString)),
      order => {
        require(order.quantity > 0, "Quantity must be positive.")
        require(order.pricePerUnit > 0, "Price must be positive.")
        ordersService.registerOrder(order).map(saved => Ok(Json.toJson(saved)))
      }
    )
  }

  def deleteOrder(orderId: String) = Action.async {
    ordersService.deleteOrder(orderId).map {
      case None => NotFound(s"order $orderId does not exist")
      case Some(deletedOrder) => Ok
    }
  }

  def getOrdersSummary = Action.async { request =>
    ordersService.getOrderSummary().map(summaries => {
      val toPrint = List("SELL:", "-----") ::: listOrMessage(summaries._1) ::: List("", "BUY:", "----") ::: listOrMessage(summaries._2)
      Ok(toPrint.mkString(Properties.lineSeparator))
    })
  }

  private def listOrMessage(list: List[OrderSummary]) = {
    if (list.isEmpty) List("There are no orders.") else list.map(_.toString())
  }
}