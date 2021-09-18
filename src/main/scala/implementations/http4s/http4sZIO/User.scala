package implementations.http4s.http4sZIO

import zio.{Has, RIO, ZIO}

object User {
  type Configuration = Has[Configuration.Service]
  case class Config(api: ApiConfig, dbConfig: DbConfig)
  case class ApiConfig(endpoint: String, port: Int)
  case class DbConfig(url: String, user: String, password: String)
  def loadConfig: RIO[Configuration, Config] = ZIO.accessM(_.get.load)
}
