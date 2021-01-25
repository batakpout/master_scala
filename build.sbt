name := "com/aamir"

version := "0.1"

scalaVersion := "2.13.4"

val akkaVersion = "2.6.11"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.typelevel" %% "cats-core" % "2.1.0",
  "org.scalatest" %% "scalatest" % "3.2.3",
  "org.scalatest" %% "scalatest-wordspec" % "3.2.3",
  "org.apache.kafka" %% "kafka" % "2.7.0",
  "com.typesafe.play" %% "play" % "2.8.7",
  "org.json4s" %% "json4s-jackson" % "3.6.10",
  "org.mockito" % "mockito-core" % "3.7.7" % Test,
  "org.scalamock" %% "scalamock" % "5.1.0" % Test,
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.0"



)
