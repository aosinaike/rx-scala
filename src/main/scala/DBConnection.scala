import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

//https://stackoverflow.com/questions/47243614/slick-with-postgresql-scala-sbt-intellij-idea

object DBConnection {
  case class Song(
                   id: Int,
                   name: String,
                   singer: String)

  class SongsTable(tag: Tag) extends Table[Song](tag, "songs") {
    def id = column[Int]("id")

    def name = column[String]("name")

    def singer = column[String]("singer")

    def * = (id, name, singer) <> (Song.tupled, Song.unapply)
  }

  val db = Database.forConfig("scalaxdb")

  val songs = TableQuery[SongsTable]

  def main(args: Array[String]): Unit = {
    Await.result({
      db.run(songs.result).map(_.foreach(row =>
        println("song with id " + row.id + " has name " + row.name + " and a singer is " + row.singer)))
    }, 1 minute)
  }
}
