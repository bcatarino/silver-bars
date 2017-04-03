import model.Order
import objects.Orders._
import org.apache.http.entity.ContentType
import play.api.libs.json.Json
import play.api.test._

class RoutesSpec extends PlaySpecification {

  "POST /orders" should {

    "send 400 if no body provided" in new WithApplication() {
      val request = FakeRequest(POST, "/orders").withHeaders(CONTENT_TYPE -> ContentType.APPLICATION_JSON.toString)
      val Some(result) = route(app, request)
      status(result) must_== BAD_REQUEST
    }

    "send 500 if required field not present" in new WithApplication() {

      val jsValue = Json.obj(Order.USER_ID -> buyOrder1.userId,
        Order.QUANTITY -> buyOrder1.quantity, Order.ORDER_TYPE -> buyOrder1.orderType)

      val request = FakeRequest(POST, "/orders").withBody(jsValue).withHeaders(CONTENT_TYPE -> ContentType.APPLICATION_JSON.toString)

      val Some(result) = route(app, request)
      status(result) must_== INTERNAL_SERVER_ERROR
    }

    "send 200 if everything's ok" in new WithApplication() {

      val jsValue = Json.obj(Order.USER_ID -> buyOrder1.userId, Order.PRICE_PER_UNIT -> buyOrder1.pricePerUnit,
        Order.QUANTITY -> buyOrder1.quantity, Order.ORDER_TYPE -> buyOrder1.orderType)

      val request = FakeRequest(POST, "/orders").withBody(jsValue).withHeaders(CONTENT_TYPE -> ContentType.APPLICATION_JSON.toString)

      val Some(result) = route(app, request)
      status(result) must_== OK
    }
  }

  "GET /orders/summary" should {

    "send 200" in new WithApplication() {
      val request = FakeRequest(GET, "/orders/summary").withHeaders(CONTENT_TYPE -> ContentType.APPLICATION_JSON.toString)
      val Some(result) = route(app, request)
      status(result) must_== OK
    }
  }

  "DELETE /order/:orderId" should {

    "send 404 if order id not provided" in new WithApplication() {
      val request = FakeRequest(DELETE, "/order/").withHeaders(CONTENT_TYPE -> ContentType.APPLICATION_JSON.toString)
      val Some(result) = route(app, request)
      status(result) must_== NOT_FOUND
    }

    "send 404 if order does not exist" in new WithApplication() {

      val request = FakeRequest(DELETE, "/order/123").withHeaders(CONTENT_TYPE -> ContentType.APPLICATION_JSON.toString)

      val Some(result) = route(app, request)
      status(result) must_== NOT_FOUND
    }

    "send 200 if it can delete order" in new WithApplication() {

      val jsValue = Json.obj(Order.USER_ID -> buyOrder1.userId, Order.PRICE_PER_UNIT -> buyOrder1.pricePerUnit,
        Order.QUANTITY -> buyOrder1.quantity, Order.ORDER_TYPE -> buyOrder1.orderType)

      val fakePost = FakeRequest(POST, "/orders").withBody(jsValue).withHeaders(CONTENT_TYPE -> ContentType.APPLICATION_JSON.toString)
      val Some(postResult) = route(app, fakePost)
      val savedOrder = contentAsJson(postResult).as[Order]

      val fakeDelete = FakeRequest(DELETE, "/order/" + savedOrder.orderId).withHeaders(CONTENT_TYPE -> ContentType.APPLICATION_JSON.toString)
      val Some(result) = route(app, fakePost)
      status(result) must_== OK
    }
  }
}
