package implementations.http4s.http4sCats

import cats._
import cats.effect._
import cats.implicits._
import org.http4s.circe._
import org.http4s._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl._
import org.http4s.dsl.impl._
import org.http4s.headers._
import org.http4s.implicits._
import org.http4s.server._
import org.http4s.server.blaze.BlazeServerBuilder

import java.time.Year
import java.util.UUID
import scala.collection.mutable
import scala.util.Try

object IMDB extends IOApp {

  //Domain
  type Actor = String
  case class Movie(id: String, title: String, year: Int, actors: List[Actor], director: String)
  case class Director(firstName: String, lastName: String){
    override def toString: Actor = s"$firstName $lastName"
  }
  case class DirectorDetails(firstName: String, lastName: String, genre: String)

  //Internal database
  val snjl: Movie = Movie(
    "6bcbca1e-efd3-411d-9f7c-14b872444fce",
    "Zack Snyder's Justice League",
    2021,
    List("Henry Cavill", "Gal Godot", "Ezra Miller", "Ben Affleck", "Ray Fisher", "Jason Momoa"),
    "Zack Snyder"
  )

  val movies: Map[String, Movie] = Map(snjl.id -> snjl)

  //business logic
  private def findMovieById(movieId: UUID) =
    movies.get(movieId.toString)

  private def findMoviesByDirector(director: String): List[Movie] =
    movies.values.filter(_.director == director).toList
  /*
  -GET All movies by director
  -GET All actors for a movie
   */

  /*
  GET movies?director=zack%20Synder&year=2021
   */

 //scala 3 use given
//  given val yearQueryParamDecoder: QueryParamDecoder[Year] =
//    QueryParamDecoder[Int].map(Year.of)

  implicit val yearQueryParamDecoder: QueryParamDecoder[Year] =
    QueryParamDecoder[Int].emap { y =>
      Try(Year.of(y))
        .toEither
        .leftMap { tr =>
          ParseFailure(tr.getMessage, tr.getMessage)
        }
    }
  object DirectorQueryParamMatcher extends QueryParamDecoderMatcher[String]("director")
  object YearQueryParamMatcher extends OptionalValidatingQueryParamDecoderMatcher[Year]("year")

  def movieRoutes[F[_]: Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "movies" :? DirectorQueryParamMatcher(director) +& YearQueryParamMatcher(maybeYear) =>
        val moviesByDirector = findMoviesByDirector(director)
        maybeYear match {
          case Some(y) =>
            y.fold(
              _ => BadRequest("The given year is not valid"),
              year => {

                val moviesByDirectorAndYear = moviesByDirector.filter(_.year == year.getValue())
                Ok(moviesByDirectorAndYear.asJson)
              }
            )
          case None => Ok(moviesByDirector.asJson)
        }
      case GET -> Root / "movies" / UUIDVar(movieId) / "actors" =>
        findMovieById(movieId).map(_.actors) match {
          case Some(actors) => Ok(actors.asJson)
          case _ => NotFound(s"No movie with id $movieId found")
        }
    }
  }

  object DirectorVar {
    def unapply(str: String): Option[Director] = {
      if (str.nonEmpty && str.matches(".* .*")) {
        Try {
          val splitStr = str.split(' ')
          Director(splitStr(0), splitStr(1))
        }.toOption
      } else None
    }
  }

  val directorDetailsDB: mutable.Map[Director, DirectorDetails] =
    mutable.Map(Director("Zack", "Zynder")-> DirectorDetails("Zack", "Snyder", "Action"))

  def directorRoutes[F[_] : Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "directors" / DirectorVar(director) =>
        directorDetailsDB.get(director) match {
          case Some(dirDetails) => Ok(dirDetails.asJson)
          case _ => NotFound(s"No director '$director' Found")
        }
    }
  }

  def allRoutes[F[_] : Monad]: HttpRoutes[F] = {
    import cats.syntax.semigroupk._
    movieRoutes[F] <+> directorRoutes[F]
  }

  def allRoutesComplete[F[_] : Monad]: HttpApp[F] = {
    allRoutes[F].orNotFound
  }

  override def run(args: List[String]): IO[ExitCode] = {

    val apis = Router(
      "/api" -> IMDB.movieRoutes[IO],
      "/api/private" -> IMDB.directorRoutes[IO]
    ).orNotFound

    BlazeServerBuilder[IO](runtime.compute)
      .bindHttp(8089, "localhost")
      .withHttpApp(allRoutesComplete)//.withHttpApp(apis)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
