package implementations.akka.controller

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import cats.effect.IO
import implementations.akka.controller.EmployeeController.QueryEmployee
import implementations.akka.repository.EmployeeRepository
import implementations.akka.repository.EmployeeRepository.{Address, Employee}
import spray.json.DefaultJsonProtocol

import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object EmployeeController {
  case class QueryEmployee(id: String,
                           firstName: String,
                           lastName: String)

  object EmployeeJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {

    implicit val addressFormat = jsonFormat5(Address.apply)
    implicit val employeeFormat = jsonFormat5(Employee.apply)
    implicit val employeeQueryFormat = jsonFormat3(QueryEmployee.apply)
  }
}


trait EmployeeController
  extends EmployeeRepository {

  /**
   * The Actor system to be used by the Future Context.
   *
   * @return
   */
  implicit def actorSystem: ActorSystem

  /**
   * Logging using the actor system.
   */
  lazy val logger = Logging(actorSystem, classOf[EmployeeController])

  import EmployeeRepository._
  import implementations.akka.controller.EmployeeController.EmployeeJsonProtocol._

  /**
   * Employee Routes for the GET/POST/Other REST endpoints for the Employee endpoints.
   */
  lazy val employeeRoutes: Route = pathPrefix("employee") {
    get {
      path(Segment) { id =>
        onComplete(getEmployeeById(id)) {
          _ match {
            case Success(employee) =>
              logger.info(s"Got the employee records given the employee id ${id}")
              complete(StatusCodes.OK, employee)
            case Failure(throwable) =>
              logger.error(s"Failed to get the employee record given the employee id ${id}")
              throwable match {
                case e: EmployeeNotFoundException => complete(StatusCodes.NotFound, "No employee found")
                case e: DubiousEmployeeRecordsException => complete(StatusCodes.NotFound, "Dubious records found.")
                case _ => complete(StatusCodes.InternalServerError, "Failed to get the employees.")
              }
          }
        }
      }
    } ~ post {
      path("query") {
        entity(as[QueryEmployee]) {
          q =>
          onComplete(queryEmployee(q.id, q.firstName, q.lastName)) {
            _ match {
              case Success(employees) =>
                logger.info("Got the employee records for the search query.")
                complete(StatusCodes.OK, employees)
              case Failure(throwable) =>
                logger.error("Failed to get the employees with the given query condition.")
                complete(StatusCodes.InternalServerError, "Failed to query the employees.")
            }
          }
//
//          for{
//            employees <- queryEmployeeList(q.id, q.firstName, q.lastName)
//            _ <- IO.sleep(2.seconds)
//          } yield { employees }
        }
      }
    }
  }
}


