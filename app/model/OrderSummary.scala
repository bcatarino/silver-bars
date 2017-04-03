package model

case class OrderSummary(price: Double, quantity: Double) {
  override def toString() = {
    s"$quantity kg for £$price"
  }
}