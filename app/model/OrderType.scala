package model

import play.api.libs.json._

sealed trait OrderType

case object Buy extends OrderType
case object Sell extends OrderType

object OrderType {
  implicit val orderTypeFormatter = new Format[OrderType] {
    def reads(json: JsValue) = json match {
      case JsString(t: String) =>
        t.toUpperCase match {
          case "BUY" => JsSuccess(Buy)
          case "SELL" => JsSuccess(Sell)
          case _ => JsError(s"Unknown order type '$t'")
        }
      case _ => JsError(s"Unexpected JSON value $json")
    }

    def writes(orderType: OrderType): JsValue = {
      orderType match {
        case Buy => JsString("BUY")
        case Sell => JsString("SELL")
      }
    }
  }
}