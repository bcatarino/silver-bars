package model

import play.api.libs.json.Json

case class Order(userId: String, quantity: Double, pricePerUnit: Double,
                 orderType: OrderType, orderId: Option[String] = None)

object Order {
  import OrderType.orderTypeFormatter

  implicit val orderFormatter = Json.format[Order]

  lazy val USER_ID = "userId"
  lazy val QUANTITY = "quantity"
  lazy val PRICE_PER_UNIT = "pricePerUnit"
  lazy val ORDER_TYPE = "orderType"
  lazy val ORDER_ID = "orderId"
}