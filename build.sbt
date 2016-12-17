name := "flightaware"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "io.spray" %%  "spray-json" % "1.3.2"

//libraryDependencies += "io.spray" %%  "spray-json" % "1.3.2"

// https://mvnrepository.com/artifact/com.typesafe.akka/akka-actor_2.11

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.14"

// https://mvnrepository.com/artifact/com.typesafe.akka/akka-http_2.11
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.0.0"

libraryDependencies += "io.argonaut" %% "argonaut" % "6.1"


val circeVersion = "0.6.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
