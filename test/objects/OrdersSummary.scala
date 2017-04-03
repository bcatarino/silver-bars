package objects

import Orders._
import model.OrderSummary

object OrdersSummary {

  val sellSummaryAggr = OrderSummary(sellOrder1.pricePerUnit, sellOrder1.quantity + sellOrder3.quantity)
  val sellSummarySingle = OrderSummary(sellOrder2.pricePerUnit, sellOrder2.quantity)

  val buySummaryAggr = OrderSummary(buyOrder2.pricePerUnit, buyOrder2.quantity + buyOrder3.quantity)
  val buySummarySingle = OrderSummary(buyOrder1.pricePerUnit, buyOrder1.quantity)
}
