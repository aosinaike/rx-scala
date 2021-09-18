package implementations.zio


import zio.{Has, Task, ZIO, ZLayer}

case class User(name: String, email: String)
object EmailService  extends zio.App{

  object UserEmailer  {

    //service Def
    type userEmailerEnv = Has[UserEmailer.Service]
    trait Service {
      def notify(user: User, message: String): Task[Unit]
    }

    //Service Impl
    val live: ZLayer[Any, Nothing, userEmailerEnv] = ZLayer.succeed(new Service {
      override def notify(user: User, message: String) = Task {
        println(s"[User emailer] sending '$message' to ${user.email}")
      }
    })

    //Front-End Api
    def notify(user: User, message: String): ZIO[userEmailerEnv, Throwable, Unit] =
      ZIO.accessM(hasService => hasService.get.notify(user, message))
  }

  object UserDB {
    type userDBEnv = Has[UserDB.Service]

    trait Service {
      def insert(user: User): Task[Unit]
    }

    val live = ZLayer.succeed(new Service {
      override def insert(user: User) = Task {
        println(s"[Database] Inserting in public.user values ('${user.name}')'")
      }
    })


    def insert(user: User): ZIO[userDBEnv, Throwable, Unit] = ZIO.accessM(_.get.insert(user))
  }
  //user bank-end
  import UserDB._
  import UserEmailer._
  val userBankEndLayer: ZLayer[Any, Nothing, userDBEnv with userEmailerEnv] = UserDB.live ++ UserEmailer.live

  object UserSubscription{
    type UserSubscriptionEnv = Has[UserSubscription.Service]
    class Service (notifier: UserEmailer.Service,userDB: UserDB.Service){
      def subscribe(user: User): Task[User] =
        for {
        _ <- userDB.insert(user)
        _ <- notifier.notify(user,s"Welcome ${user.name}")
      } yield user
    }
    val live: ZLayer[userEmailerEnv with userDBEnv,Nothing, UserSubscriptionEnv] = ZLayer.fromServices[UserEmailer.Service, UserDB.Service, UserSubscription.Service]{
      (userEmailer, userDB) => new Service(userEmailer, userDB)
    }

    //Front end API
    def subscribe(user: User): ZIO[UserSubscriptionEnv, Throwable, User] =
      ZIO.accessM(_.get.subscribe(user))
  }

  import UserSubscription._
  val userSubscriptionLayer: ZLayer[Any, Nothing, UserSubscriptionEnv] = userBankEndLayer >>> UserSubscription.live

  val user = User("abbey", "abiodun@gmail.com")
  val message = "Welcome"

  def notifyUser() =
    UserEmailer.notify(user, message)
      .provideLayer(UserEmailer.live)
      .exitCode

  override def run(args: List[String]) =
    UserSubscription.subscribe(user)
      .provideLayer(userSubscriptionLayer)
      .exitCode
}

