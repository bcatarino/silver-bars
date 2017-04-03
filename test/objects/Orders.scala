package objects

import model.{Buy, Order, Sell}
import play.api.libs.json.Json

/**
  * A set of generic Order test objects that can be reused in tests.
  */
object Orders {

  val buyOrder1 = Order("eee072aa-215c-429d-b570-4c4b5bcd0f0f", 3.2, 4.2, Buy)
  val savedBuyOrder1 = buyOrder1.copy(orderId = Some("0221833a-381d-40dd-b23a-d96fd9e33a92"))

  val buyOrder1JsValue = Json.obj(Order.USER_ID -> buyOrder1.userId,
    Order.PRICE_PER_UNIT -> buyOrder1.pricePerUnit, Order.QUANTITY -> buyOrder1.quantity,
    Order.ORDER_TYPE -> buyOrder1.orderType)


  val buyOrder2 = Order("d95e811a-7989-47ba-bbf7-adc1192d128f", 3.2, 2.2, Buy)
  val savedBuyOrder2 = buyOrder2.copy(orderId = Some("0221833a-381d-40dd-b23a-d96fd9e33a92"))

  val buyOrder2JsValue = Json.obj(Order.USER_ID -> buyOrder2.userId,
    Order.PRICE_PER_UNIT -> buyOrder2.pricePerUnit, Order.QUANTITY -> buyOrder2.quantity,
    Order.ORDER_TYPE -> buyOrder2.orderType)


  val buyOrder3 = Order("bea5bf93-fdcf-4b66-a0f3-016075d8ca96", 7.4, 2.2, Buy)
  val savedBuyOrder3 = buyOrder3.copy(orderId = Some("0ca326b7-1e1f-4ae4-bfb2-c7584e9938bf"))

  val buyOrder3JsValue = Json.obj(Order.USER_ID -> buyOrder3.userId,
    Order.PRICE_PER_UNIT -> buyOrder3.pricePerUnit, Order.QUANTITY -> buyOrder3.quantity,
    Order.ORDER_TYPE -> buyOrder3.orderType)


  val sellOrder1 = Order("b8299a40-818a-4a8e-9a94-1973856bc9f2", 1.0, 3.1, Sell)
  val savedSellOrder1 = sellOrder1.copy(orderId = Some("e52780e1-caae-438c-ac2b-d53af3274a4a"))

  val sellOrder1JsValue = Json.obj(Order.USER_ID -> sellOrder1.userId,
    Order.PRICE_PER_UNIT -> sellOrder1.pricePerUnit, Order.QUANTITY -> sellOrder1.quantity,
    Order.ORDER_TYPE -> sellOrder1.orderType)


  val sellOrder2 = Order("4e53b7c5-14b1-4f96-8508-1192d71be94a", 2.8, 8.1, Sell)
  val savedSellOrder2 = sellOrder2.copy(orderId = Some("f6897187-9756-4ca4-9c50-707f05de7eae"))

  val sellOrder2JsValue = Json.obj(Order.USER_ID -> sellOrder2.userId,
    Order.PRICE_PER_UNIT -> sellOrder2.pricePerUnit, Order.QUANTITY -> sellOrder2.quantity,
    Order.ORDER_TYPE -> sellOrder2.orderType)


  val sellOrder3 = Order("973d483f-19c3-443b-a4bc-166706657c90", 15.5, 3.1, Sell)
  val savedSellOrder3 = sellOrder3.copy(orderId = Some("3d6c2d50-52a3-4c2b-8f56-59b4c694f694"))

  val sellOrder3JsValue = Json.obj(Order.USER_ID -> sellOrder3.userId,
    Order.PRICE_PER_UNIT -> sellOrder3.pricePerUnit, Order.QUANTITY -> sellOrder3.quantity,
    Order.ORDER_TYPE -> sellOrder3.orderType)
}
