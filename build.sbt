name := "rx-scala"

version := "0.1"

scalaVersion := "2.13.6"

val Http4sVersion = "1.0.0-M21"//"0.23.3"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "2.0.0-M2",
  "dev.zio" %% "zio-test" % "2.0.0-M2" % Test,
  "org.typelevel" %% "cats-core" % "2.6.1",

  "com.typesafe.akka" %% "akka-actor" % "2.6.16",
  "com.typesafe.akka" %% "akka-slf4j" % "2.6.16",
  "com.typesafe.akka" %% "akka-stream" % "2.6.16",
  "com.typesafe.akka" %% "akka-testkit" % "2.6.16" % "test",
  "com.typesafe.akka" %% "akka-http-core"  % "10.2.6",
  "com.typesafe.akka" %% "akka-http"       % "10.2.6",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.6",
  "com.typesafe.akka" %% "akka-http-xml" % "10.2.6",

  "com.typesafe.slick" %% "slick" % "3.3.3",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",

  "org.postgresql" % "postgresql" % "42.2.23",
  "org.slf4j" % "slf4j-nop" % "1.7.32",
  "ch.qos.logback" % "logback-classic" % "1.2.5",
  "net.liftweb" %% "lift-json" % "3.4.3",

  "io.circe" %% "circe-generic" % "0.14.1",
  "io.circe" %% "circe-core" % "0.14.1",
  "io.circe" %% "circe-parser" % "0.14.1",
  "com.twitter" %% "twitter-server" % "21.8.0",

  "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s" %% "http4s-dsl"          % Http4sVersion,
  "org.http4s" %% "http4s-circe"        % Http4sVersion,

  "org.tpolecat" %% "doobie-core"      % "1.0.0-RC1",
  "org.tpolecat" %% "doobie-h2" % "1.0.0-RC1",
  "org.tpolecat" %% "doobie-postgres" % "1.0.0-RC1",
  "org.tpolecat" %% "doobie-postgres-circe" % "1.0.0-RC1",
  "com.github.pureconfig" %% "pureconfig" % "0.16.0"
)

//scalacOptions := Seq("-unchecked", "-deprecation")
scalacOptions += "-deprecation"