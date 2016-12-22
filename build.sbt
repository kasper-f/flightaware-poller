
name := "flightaware"

version := "1.0"

scalaVersion := "2.11.8"

enablePlugins(JavaAppPackaging)

mainClass in Compile := Some("dk.kaab.flightaware.Flightaware")

enablePlugins(JavaServerAppPackaging)

enablePlugins(DebianPlugin)

serverLoading in Debian := com.typesafe.sbt.packager.archetypes.ServerLoader.Systemd

requiredStartFacilities in Debian := Some("network.target")

maintainer := "kaab"

debianPackageDependencies in Debian += "openjdk-8-jre"

packageSummary in Debian:= "flightaware poller"

packageDescription in Debian:=
  """flightaware poller service, will start service and poll flightware for flights in specified areas""".stripMargin

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.14"

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.4.14" % "test"

libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.0.0"

libraryDependencies += "com.microsoft.sqlserver" % "mssql-jdbc" % "6.1.0.jre8"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc"        % "2.5.+",
  "com.h2database"  %  "h2"                 % "1.4.+",
  "ch.qos.logback"  %  "logback-classic"    % "1.1.+"
)



val circeVersion = "0.6.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
