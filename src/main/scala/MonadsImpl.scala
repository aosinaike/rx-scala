import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object MonadsImpl {


  //Validating API
  case class Person(firstName: String, lastName: String) {
    assert(firstName != null)
  }

  def getPerson(firstName: String, lastName: String): Option[Person] = for {
    fName <- Option(firstName)
    lName <- Option(lastName)
  } yield Person(fName, lName)


  // Asynchronous fetch
  case class User(id: String)

  case class Product(productId: String, price: Double)

  def getUser(url: String): Future[User] = Future {
    User("Abbey")
  }

  def getLastOrder(userId: String): Future[Product] = Future {
    Product("Samsung", 290)
  }

  val url = "my.store.com/user/abbey"

  val vatInclPrice: Future[Double] = getUser(url)
    .flatMap(user => getLastOrder(user.id))
    .map(_.price * 1.19)

  val vatInclPriceFor: Future[Double] = for {
    user <- getUser(url)
    product <- getLastOrder(user.id)
  } yield product.price * 1.19

  // Double for loops
  var numbers = List(1, 2, 3)
  var chars = List('a', 'b', 'c')

  val checkerboard: List[(Int, Char)] = numbers.flatMap(number => chars.map(char => (number, char)))
  val checkerboard2 = for {
    number <- numbers
    char <- chars
  } yield (number, char)
}
