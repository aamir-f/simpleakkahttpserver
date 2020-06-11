package Controllers

import Entities.Employee
import akka.actor.ActorRef
import exceptions.{ErrorCodes, InvalidInputException}
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse
import repositories.ImplEmployeeRepository

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}


abstract class EmployeeControllerComponent {
  def insertEmployeeController(data: String): Future[Employee]

  def getAllEmployees(): Future[Seq[Employee]]
}

object EmployeeController extends EmployeeControllerComponent {

  implicit val f = DefaultFormats

  def insertEmployeeController(data: String): Future[Employee] = {
    val employeeTry: Try[Employee] = Try(parse(data).extract[Employee])
    employeeTry match {
      case Success(s) => {
        ImplEmployeeRepository.insertItem(s)
      }
      case Failure(f) => Future.failed(InvalidInputException(ErrorCodes.INVALID_INPUT_EXCEPTION, message = "some msg", exception = new Exception(f.getCause)))
    }
  }

  def getAllEmployees() = {
    ImplEmployeeRepository.getEmployees
  }

}
