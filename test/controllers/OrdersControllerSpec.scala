package controllers

import model.Order
import model.Order._
import objects.Orders._
import objects.OrdersSummary._
import org.specs2.execute.Results
import org.specs2.mock.Mockito
import play.api.libs.json.Json
import play.api.test.{FakeRequest, PlaySpecification}
import services.OrdersService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Properties

class OrdersControllerSpec extends PlaySpecification with Mockito with Results {

  isolated

  val ordersService = mock[OrdersService]
  val controller = new OrdersController(ordersService)

  "OrdersController#registerOrder" should {

    "throw NullPointerException if no body" in {

      val request = FakeRequest().withBody(null)
      controller.registerOrder.apply(request) must throwA[NullPointerException]
    }

    "return 500 if mandatory fields not present" in {

      val request = FakeRequest().withBody(Json.obj())
      val futureResult = controller.registerOrder.apply(request)

      status(futureResult) must_== INTERNAL_SERVER_ERROR
      contentAsString(futureResult) contains "(/quantity,List(ValidationError(List(error.path.missing),WrappedArray())))"
      contentAsString(futureResult) contains "(/pricePerUnit,List(ValidationError(List(error.path.missing),WrappedArray())))"
      contentAsString(futureResult) contains "(/orderType,List(ValidationError(List(error.path.missing),WrappedArray())))"
      contentAsString(futureResult) contains "(/userId,List(ValidationError(List(error.path.missing),WrappedArray())))"
    }

    "throw IlegalArgumentException if quantity == 0" in {

      val body = Json.obj(USER_ID -> sellOrder1.userId,
        PRICE_PER_UNIT -> sellOrder1.pricePerUnit, QUANTITY -> 0,
        ORDER_TYPE -> sellOrder1.orderType)

      val request = FakeRequest().withBody(body)
      controller.registerOrder.apply(request) must throwA[IllegalArgumentException]
    }

    "throw IlegalArgumentException if quantity < 0" in {

      val body = Json.obj(USER_ID -> sellOrder1.userId,
        PRICE_PER_UNIT -> sellOrder1.pricePerUnit, QUANTITY -> -0.1,
        ORDER_TYPE -> sellOrder1.orderType)

      val request = FakeRequest().withBody(body)
      controller.registerOrder.apply(request) must throwA[IllegalArgumentException]
    }

    "throw IlegalArgumentException if pricePerUnit == 0" in {

      val body = Json.obj(USER_ID -> sellOrder1.userId,
        PRICE_PER_UNIT -> 0, QUANTITY -> sellOrder1.quantity,
        ORDER_TYPE -> sellOrder1.orderType)

      val request = FakeRequest().withBody(body)
      controller.registerOrder.apply(request) must throwA[IllegalArgumentException]
    }

    "throw IlegalArgumentException if pricePerUnit < 0" in {

      val body = Json.obj(USER_ID -> sellOrder1.userId,
        PRICE_PER_UNIT -> -2, QUANTITY -> sellOrder1.quantity,
        ORDER_TYPE -> sellOrder1.orderType)

      val request = FakeRequest().withBody(body)
      controller.registerOrder.apply(request) must throwA[IllegalArgumentException]
    }

    "throw IlegalArgumentException if pricePerUnit == 0" in {

      val body = Json.obj(USER_ID -> sellOrder1.userId,
        PRICE_PER_UNIT -> 0, QUANTITY -> sellOrder1.quantity,
        ORDER_TYPE -> sellOrder1.orderType)

      val request = FakeRequest().withBody(body)
      controller.registerOrder.apply(request) must throwA[IllegalArgumentException]
    }

    "throw IlegalArgumentException if pricePerUnit < 0" in {

      val body = Json.obj(USER_ID -> sellOrder1.userId,
        PRICE_PER_UNIT -> -2, QUANTITY -> sellOrder1.quantity,
        ORDER_TYPE -> sellOrder1.orderType)

      val request = FakeRequest().withBody(body)
      controller.registerOrder.apply(request) must throwA[IllegalArgumentException]
    }

    "return 500 if order type invalid" in {

      val body = Json.obj(USER_ID -> sellOrder1.userId,
        PRICE_PER_UNIT -> sellOrder1.pricePerUnit, QUANTITY -> sellOrder1.quantity,
        ORDER_TYPE -> "BLAH")

      val request = FakeRequest().withBody(body)
      val futureResult = controller.registerOrder.apply(request)

      status(futureResult) must_== INTERNAL_SERVER_ERROR
      contentAsString(futureResult) contains "(/orderType,List(ValidationError(List(Unknown order type 'BLAH'),WrappedArray())))"
    }

    "return 200 if all SELL input is valid" in {

      val body = Json.obj(USER_ID -> sellOrder1.userId,
        PRICE_PER_UNIT -> sellOrder1.pricePerUnit, QUANTITY -> sellOrder1.quantity,
        ORDER_TYPE -> sellOrder1.orderType)

      ordersService.registerOrder(sellOrder1) returns Future(savedSellOrder1)

      val request = FakeRequest().withBody(body)
      val futureResult = controller.registerOrder.apply(request)

      status(futureResult) must_== OK
      contentAsJson(futureResult) must_== Json.toJson(savedSellOrder1)
    }

    "return 200 if all BUY input is valid" in {

      val body = Json.obj(USER_ID -> buyOrder1.userId,
        PRICE_PER_UNIT -> buyOrder1.pricePerUnit, QUANTITY -> buyOrder1.quantity,
        ORDER_TYPE -> buyOrder1.orderType)

      ordersService.registerOrder(buyOrder1) returns Future(savedBuyOrder1)

      val request = FakeRequest().withBody(body)
      val futureResult = controller.registerOrder.apply(request)

      status(futureResult) must_== OK
      contentAsJson(futureResult) must_== Json.toJson(savedBuyOrder1)
    }

    "throw NullPointerException if service returns null" in {

      val body = Json.obj(USER_ID -> sellOrder1.userId,
        PRICE_PER_UNIT -> sellOrder1.pricePerUnit, QUANTITY -> sellOrder1.quantity,
        ORDER_TYPE -> sellOrder1.orderType)

      val request = FakeRequest().withBody(body)
      controller.registerOrder.apply(request) must throwA[NullPointerException]
    }

    "call service exactly once" in {

      val body = Json.obj(USER_ID -> buyOrder1.userId,
        PRICE_PER_UNIT -> buyOrder1.pricePerUnit, QUANTITY -> buyOrder1.quantity,
        ORDER_TYPE -> buyOrder1.orderType)

      ordersService.registerOrder(buyOrder1) returns Future(savedBuyOrder1)

      val request = FakeRequest().withBody(body)
      controller.registerOrder.apply(request)

      there was one(ordersService).registerOrder(any[Order])
    }
  }

  "OrdersController#deleteOrder" should {

    "throw NullPointerException if service returns null" in {
      controller.deleteOrder(savedBuyOrder1.orderId.get).apply(FakeRequest()) must throwA[NullPointerException]
    }

    "return 404 if order with id does not exist" in {
      ordersService.deleteOrder(savedBuyOrder1.orderId.get) returns Future(None)
      val futureResult = controller.deleteOrder(savedBuyOrder1.orderId.get).apply(FakeRequest())
      status(futureResult) must_== NOT_FOUND
    }

    "return 200 if order is deleted" in {
      ordersService.deleteOrder(savedBuyOrder1.orderId.get) returns Future(Some(savedBuyOrder1))
      val futureResult = controller.deleteOrder(savedBuyOrder1.orderId.get).apply(FakeRequest())
      status(futureResult) must_== OK
    }

    "call service exactly once" in {
      ordersService.deleteOrder(savedBuyOrder1.orderId.get) returns Future(Some(savedBuyOrder1))
      controller.deleteOrder(savedBuyOrder1.orderId.get).apply(FakeRequest())
      there was one(ordersService).deleteOrder(any[String])
    }
  }

  "OrdersController#getOrdersSummary" should {

    "throw NullPointerException if service returns null" in {
      controller.getOrdersSummary().apply(FakeRequest()) must throwA[NullPointerException]
    }

    "return 200 and standard message if no data is found" in {

      ordersService.getOrderSummary() returns Future((List(), List()))

      val futureResult = controller.getOrdersSummary().apply(FakeRequest())

      status(futureResult) must_== OK
      contentAsString(futureResult) must_== List("SELL:", "-----", "There are no orders.", "", "BUY:", "----",
        "There are no orders.").mkString(Properties.lineSeparator)
    }

    "return 200 and single sell order" in {

      ordersService.getOrderSummary() returns Future((List(sellSummaryAggr), List()))

      val futureResult = controller.getOrdersSummary().apply(FakeRequest())

      status(futureResult) must_== OK
      contentAsString(futureResult) must_== List("SELL:", "-----", sellSummaryAggr.toString(), "", "BUY:", "----",
        "There are no orders.").mkString(Properties.lineSeparator)
    }

    "return 200 and two buy orders" in {

      ordersService.getOrderSummary() returns Future((List(), List(buySummaryAggr, buySummarySingle)))

      val futureResult = controller.getOrdersSummary().apply(FakeRequest())

      status(futureResult) must_== OK
      contentAsString(futureResult) must_== List("SELL:", "-----", "There are no orders.", "", "BUY:", "----",
        buySummaryAggr.toString(), buySummarySingle.toString()).mkString(Properties.lineSeparator)
    }

    "return 200 and both buy and sell orders" in {

      ordersService.getOrderSummary() returns Future((List(sellSummarySingle), List(buySummaryAggr, buySummarySingle)))

      val futureResult = controller.getOrdersSummary().apply(FakeRequest())

      status(futureResult) must_== OK
      contentAsString(futureResult) must_== List("SELL:", "-----", sellSummarySingle.toString(), "", "BUY:", "----",
        buySummaryAggr.toString(), buySummarySingle.toString()).mkString(Properties.lineSeparator)
    }

    "call service exactly once" in {
      ordersService.getOrderSummary() returns Future((List(), List()))
      controller.getOrdersSummary().apply(FakeRequest())
      there was one(ordersService).getOrderSummary()
    }
  }
}