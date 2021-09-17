name := "rx-scala"

version := "0.1"

scalaVersion := "2.13.6"

val Http4sVersion = "1.0.0-M21"//"0.23.3"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "2.0.0-M2",
  "dev.zio" %% "zio-test" % "2.0.0-M2" % Test,
  "org.typelevel" %% "cats-core" % "2.6.1",

  "org.postgresql" % "postgresql" % "42.2.23",
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
  "org.slf4j" % "slf4j-nop" % "1.7.32",

  "net.liftweb" %% "lift-json" % "3.4.3",

  "com.typesafe.akka" %% "akka-actor" % "2.6.16",
  "com.typesafe.akka" %% "akka-slf4j" % "2.6.16",
  "ch.qos.logback" % "logback-classic" % "1.2.5",

  "io.circe" %% "circe-generic" % "0.14.1",
  "io.circe" %% "circe-core" % "0.14.1",
  "io.circe" %% "circe-parser" % "0.14.1",
  "com.twitter" %% "twitter-server" % "21.8.0",

  "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s" %% "http4s-dsl"          % Http4sVersion,
  "org.http4s" %% "http4s-circe"        % Http4sVersion,
)

//resolvers ++= Seq(
//  "Spray repository" at "http://repo.spray.io",
//  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
//)