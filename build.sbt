name := "udemy"

version := "0.1"

scalaVersion := "2.12.7"

val akkaVersion = "2.5.13"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.typelevel" %% "cats-core" % "2.1.0",
  "org.scalatest" %% "scalatest" % "3.0.5",
  "org.apache.kafka" %% "kafka" % "2.1.0"

)
