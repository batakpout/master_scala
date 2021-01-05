name := "com/aamir"

version := "0.1"

scalaVersion := "2.12.7"

val akkaVersion = "2.6.5"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.typelevel" %% "cats-core" % "2.1.0",
  "org.scalatest" %% "scalatest" % "3.0.5",
  "org.apache.kafka" %% "kafka" % "2.1.0",
  "com.typesafe.play" %% "play" % "2.8.2",
  "org.json4s" %% "json4s-jackson" % "3.5.5",
 "org.mockito" % "mockito-core" % "3.3.3" % Test,
  "org.scalamock" %% "scalamock" % "5.0.0" % Test



)
