package services

import model.Order
import objects.Orders._
import objects.OrdersSummary._
import org.scalatest.concurrent.ScalaFutures
import org.specs2.mock.Mockito
import persistence.OrdersDao
import play.api.test.PlaySpecification

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class OrdersServiceSpec extends PlaySpecification with Mockito with ScalaFutures {

  isolated
  sequential

  val ordersDao = mock[OrdersDao]
  val service = new OrdersService(ordersDao)

  "OrdersService#registerOrder" should {
    "calls orders dao to register exactly once" in {
      service.registerOrder(sellOrder1)
      there was one(ordersDao).registerOrder(any[Order])
    }
  }

  "OrdersService#deleteOrder" should {
    "calls orders dao to delete exactly once" in {
      service.deleteOrder(savedSellOrder1.orderId.get)
      there was one(ordersDao).deleteOrder(any[String])
    }
  }

  "OrdersService#getOrderSummary" should {

    "return tuple with empty lists if database returns no buys or sells" in {

      ordersDao.getSellOrders returns Future(List())
      ordersDao.getBuyOrders returns Future(List())

      val result = service.getOrderSummary().futureValue

      result._1 must_== List()
      result._2 must_== List()
    }

    "sums sell orders of same value" in {

      ordersDao.getSellOrders returns Future(List(savedSellOrder1, savedSellOrder3))
      ordersDao.getBuyOrders returns Future(List())

      val result = service.getOrderSummary().futureValue

      result._1 must_== List(sellSummaryAggr)
      result._2 must_== List()
    }

    "sums buy orders of same value" in {

      ordersDao.getSellOrders returns Future(List())
      ordersDao.getBuyOrders returns Future(List(savedBuyOrder2, savedBuyOrder3))

      val result = service.getOrderSummary().futureValue

      result._1 must_== List()
      result._2 must_== List(buySummaryAggr)
    }

    "sorts sell orders ASC" in {

      ordersDao.getSellOrders returns Future(List(savedSellOrder1, savedSellOrder2, savedSellOrder3))
      ordersDao.getBuyOrders returns Future(List())

      val result = service.getOrderSummary().futureValue

      result._1 must_== List(sellSummaryAggr, sellSummarySingle)
      result._2 must_== List()
    }

    "sorts buy orders DESC" in {

      ordersDao.getSellOrders returns Future(List())
      ordersDao.getBuyOrders returns Future(List(savedBuyOrder1, savedBuyOrder2, savedBuyOrder3))

      val result = service.getOrderSummary().futureValue

      result._1 must_== List()
      result._2 must_== List(buySummarySingle, buySummaryAggr)
    }

    "returns both sell and buy orders in tuple" in {

      ordersDao.getSellOrders returns Future(List(savedSellOrder1, savedSellOrder2, savedSellOrder3))
      ordersDao.getBuyOrders returns Future(List(savedBuyOrder1, savedBuyOrder2, savedBuyOrder3))

      val result = service.getOrderSummary().futureValue

      result._1 must_== List(sellSummaryAggr, sellSummarySingle)
      result._2 must_== List(buySummarySingle, buySummaryAggr)
    }
  }
}
