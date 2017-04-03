package persistence

import model.Order

import scala.concurrent.Future

trait OrdersDao {

  /**
    * Assigns a unique orderId to the order and stores it in the database.
    * @param order Order to save.
    * @return The saved order, with a valid unique identifier.
    */
  def registerOrder(order: Order): Future[Order]

  /**
    * Deletes the order with given orderId.
    * @param orderId key to find order to delete.
    * @return Some(order) if the order has been deleted. None if no order was deleted.
    */
  def deleteOrder(orderId: String): Future[Option[Order]]

  /**
    * Retrieves all the buy orders in the system.
    * @return
    */
  def getBuyOrders: Future[List[Order]]

  /**
    * Retrieves all the sell orders in the system.
    * @return
    */
  def getSellOrders: Future[List[Order]]
}
