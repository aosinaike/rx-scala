name := "rx-scala"

version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "2.0.0-M2",
  "dev.zio" %% "zio-test" % "2.0.0-M2" % Test,
  "org.postgresql" % "postgresql" % "42.2.23",
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
  "org.slf4j" % "slf4j-nop" % "1.7.32"
)