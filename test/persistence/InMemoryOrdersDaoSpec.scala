package persistence

import objects.Orders._
import org.scalatest.concurrent.ScalaFutures
import org.specs2.mock.Mockito
import play.api.test.PlaySpecification

class InMemoryOrdersDaoSpec extends PlaySpecification with Mockito with ScalaFutures {

  isolated

  val dao = new InMemoryOrdersDao()

  "InMemoryOrdersDao#registerOrder" should {
    "assign orderId when saving new order" in {

      sellOrder1.orderId must_== None

      val savedOrder = dao.registerOrder(sellOrder1).futureValue
      savedOrder.orderId.isDefined must_== true
    }

    "return saved order" in {
      val savedOrder = dao.registerOrder(sellOrder1).futureValue
      sellOrder1.copy(orderId = savedOrder.orderId) must_== savedOrder
    }
  }

  "InMemoryOrdersDao#getSellOrders" should {
    "return empty list if db list is empty" in {
      dao.getSellOrders.futureValue must_== List()
    }

    "return empty list if only buy orders exist" in {
      dao.registerOrder(buyOrder1)
      dao.getSellOrders.futureValue must_== List()
    }

    "return all sell orders" in {

      val savedSellOrder1 = dao.registerOrder(sellOrder1).futureValue
      val savedSellOrder2 = dao.registerOrder(sellOrder2).futureValue

      val sellOrders = dao.getSellOrders.futureValue
      sellOrders must_== List(savedSellOrder1, savedSellOrder2)
    }
  }

  "InMemoryOrdersDao#getBuyOrders" should {
    "return empty list if db list is empty" in {
      dao.getBuyOrders.futureValue must_== List()
    }

    "return empty list if only buy orders exist" in {
      dao.registerOrder(sellOrder1)
      dao.getBuyOrders.futureValue must_== List()
    }

    "return all buy orders" in {

      val savedBuyOrder1 = dao.registerOrder(buyOrder1).futureValue
      val savedBuyOrder2 = dao.registerOrder(buyOrder2).futureValue

      val buyOrders = dao.getBuyOrders.futureValue
      buyOrders must_== List(savedBuyOrder1, savedBuyOrder2)
    }
  }

  "InMemoryOrdersDao#deleteOrder" should {

    "return None if list is empty" in {
      val deletedOrder = dao.deleteOrder(savedSellOrder1.orderId.get).futureValue
      deletedOrder must_== None
    }

    "return order if order with id exists" in {
      val savedOrder = dao.registerOrder(savedSellOrder1).futureValue
      val deletedOrder = dao.deleteOrder(savedOrder.orderId.get).futureValue
      deletedOrder.get must_== savedOrder
    }

    "return None if order with id doesn't exist" in {
      dao.registerOrder(savedSellOrder1).futureValue
      val deletedOrder = dao.deleteOrder("some-other-id").futureValue
      deletedOrder must_== None
    }

    "remove order from list" in {

      dao.getSellOrders.futureValue must_== List()

      val savedOrder = dao.registerOrder(savedSellOrder1).futureValue
      dao.getSellOrders.futureValue must_== List(savedOrder)

      val deletedOrder = dao.deleteOrder(savedOrder.orderId.get).futureValue
      dao.getSellOrders.futureValue must_== List()
    }
  }
}
