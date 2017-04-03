package services

import javax.inject.Inject

import model.{Order, OrderSummary}
import persistence.OrdersDao

import scala.concurrent.ExecutionContext.Implicits.global

class OrdersService @Inject() (ordersDao: OrdersDao) {

  def registerOrder(order: Order) = ordersDao.registerOrder(order)

  def deleteOrder(orderId: String) = ordersDao.deleteOrder(orderId)

  def getOrderSummary() = {
    for {
      buyOrders <- ordersDao.getBuyOrders
      sellOrders <- ordersDao.getSellOrders
    } yield (sumOrdersForPrice(sellOrders).sortWith(_.price < _.price),
             sumOrdersForPrice(buyOrders).sortWith(_.price > _.price))
  }

  private def sumOrdersForPrice(orders: List[Order]) = {
    orders.groupBy(_.pricePerUnit).map(entry =>
      OrderSummary(entry._1, entry._2.map(_.quantity).sum)
    ).toList
  }
}
