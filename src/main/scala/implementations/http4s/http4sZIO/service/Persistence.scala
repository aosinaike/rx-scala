package implementations.http4s.http4sZIO.service

import zio.Task

object Persistence {
  trait Service[A] {
    def get(id: Int): Task[A]
    def create(user: User): Task[A]
    def delete(id: Int): Task[Boolean]
  }
}