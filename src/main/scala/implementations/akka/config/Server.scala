package implementations.akka.config

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import implementations.akka.controller.EmployeeController

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Server extends App with EmployeeController {

  implicit val actorSystem = ActorSystem("AkkaHTTPExampleServices")
  implicit val materializer = ActorMaterializer()

  lazy val apiRoutes: Route = pathPrefix("api") {
    employeeRoutes
  }

  Http().bindAndHandle(apiRoutes, "localhost", 8088)
  logger.info("Starting the HTTP server at 8089")
  Await.result(actorSystem.whenTerminated, Duration.Inf)
}

