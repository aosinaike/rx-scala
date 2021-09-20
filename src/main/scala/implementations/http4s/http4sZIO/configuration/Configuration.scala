package implementations.http4s.http4sZIO.configuration

import pureconfig.loadConfigOrThrow
import zio.{Layer, Task, ZLayer}

object Configuration {
  trait Service {
    val load: Task[Config]
  }

  trait Live extends Configuration.Service {

    import pureconfig.generic.auto._

    val load: Task[Config] = Task.effect(loadConfigOrThrow[Config])
  }


  val live: Layer[Nothing, Configuration] = ZLayer.succeed(new Live {})

  val test = ZLayer.succeed(new Service {
    val load: Task[Config] = Task.effectTotal(
      Config(ApiConfig("loacalhost", 8080), DbConfig("localhost", "", "")))
  })

}
