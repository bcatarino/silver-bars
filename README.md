# silver-bars

REST API for the Silver Bars Marketplace system, implemented using Play Framework 2.

Project was created using typesafe activator, but everything required to run it is already bundled, so only sbt is needed.

## Run Server

> sbt run

It will deploy application locally on port 9000.

## Run Tests

> sbt test

## Assumptions and Design Decisions

* Used play simply because I'm more familiar with it than Spray or other alternatives.
* Support for multiple unit types not considered at the time. Assuming it's always Kg, but should be fairly simple to extend.
* Any implementation of OrdersDao with an actual databased would most likely return a Future, so the interface was designed with that in mind.
* Chose type string for the ids, because I assumed they would be UUIDs instead of sequential numbers.
* The services layer/package is not really necessary due to the simplicity of the code right now, but it provides better separation of concerns and maintainability in the future.
* OrdersController.getOrdersSummary() is formatting because the requirements seem to demand it, but I'd usually have the REST API simply return an unformatted list of OrderSummary and let the front end decide how to format it.
* Used specs2 for tests instead of scalatest. I'm more familiar with specs2 anyway, but also had problems with dependency version clashes between scala test and a test module for play framework (despite the fact I was using the advised version for my version of scala).

## Future Improvements

* I would implement a global ErrorHandler as well to translate exceptions to specific status codes.
* I could have improved the code by putting all error messages in constants that could be reused, instead of hardcoding them.

