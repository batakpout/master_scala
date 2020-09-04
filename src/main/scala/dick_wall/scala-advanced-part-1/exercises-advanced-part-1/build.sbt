name := "Scala-Advanced-Exercises-1"

version in ThisBuild := "1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.12.4"

scalacOptions in ThisBuild ++= Seq("-deprecation", "-feature")

libraryDependencies in ThisBuild ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
  "org.scala-lang.modules" %% "scala-java8-compat" % "0.8.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "com.google.guava" % "guava" % "23.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.jsuereth"  %% "scala-arm" % "2.0",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.6",
  "org.typelevel" %% "cats-core" % "1.0.0-RC1",
  "org.typelevel" %% "cats-effect" % "0.5",
  "org.typelevel" %% "cats-free" % "1.0.0-RC1",
  "com.typesafe.play" %% "play-json" % "2.6.6",
  "org.scalactic" %% "scalactic" % "3.0.4",
  "org.scalatest" %% "scalatest" % "3.0.4" % Test,
  "com.typesafe.akka" %% "akka-actor" % "2.5.7",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.7" % Test

)

lazy val module01 = project
lazy val module02 = project
lazy val module03 = project
lazy val module04 = project
lazy val module05 = project
lazy val module06 = project
