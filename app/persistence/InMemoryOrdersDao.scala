package persistence
import java.util.UUID

import model.{Buy, Order, OrderType, Sell}

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Implementation of OrdersDao entirely in memory.
  */
class InMemoryOrdersDao extends OrdersDao {

  private val orders = ArrayBuffer.empty[Order]

  override def registerOrder(order: Order): Future[Order] = {
    val newOrder = order.copy(orderId = Some(generateOrderId()))
    orders += newOrder
    Future(newOrder)
  }

  override def deleteOrder(orderId: String): Future[Option[Order]] = {
    orders.find(_.orderId.contains(orderId)) match {
      case None =>
        Future(None)
      case Some(found) =>
        orders.remove(orders.indexOf(found))
        Future(Some(found))
    }
  }

  override def getSellOrders: Future[List[Order]] = findByType(Sell)

  override def getBuyOrders: Future[List[Order]] = findByType(Buy)

  private def findByType(t: OrderType) = Future(orders.filter(order => order.orderType.equals(t)).toList)

  private def generateOrderId() = UUID.randomUUID.toString
}
