package implementations.http4s.http4sZIO.service

final case class User(id: Long, name: String)

final case class UserNotFound(id: Int) extends Exception