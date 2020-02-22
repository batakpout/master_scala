package catz

import scala.concurrent.{Await, Future}
import scala.util.Try
import akka.util.Timeout
import cats.data.EitherT
object Program1 extends App {

  def parseDouble(s: String): Either[String, Double] = {
    Try(s.toDouble).map(Right(_)).getOrElse(Left(s"string $s is not a number"))
  }

  def divide(a: Double, b: Double): Either[String, Double] = {
    Either.cond(b != 0, a / b, "cannot divide by zero")
  }

  def divideProgram(inputA: String, inputB: String): Either[String, Double] = {
    for {
      a <- parseDouble(inputA)
      b <- parseDouble(inputB)
      res <- divide(a, b)
    } yield res
  }

  println(divideProgram("12", "2"))
  println(divideProgram("12",  "0"))
}

import scala.concurrent.ExecutionContext.Implicits.global
object Program2 extends App {

  def parseDouble(s: String): Future[Either[String, Double]] = {
    Future.successful(Try(s.toDouble).map(Right(_)).getOrElse(Left(s"string $s is not a number")))
  }

  def divide(a: Double, b: Double): Either[String, Double] = {
    Either.cond(b != 0, a / b, "cannot divide by zero")
  }

  def divideAync(a: Double, b: Double): Future[Either[String, Double]] = {
    Future.successful(divide(a, b))
  }

  def divideProgram(inputA: String, inputB: String): Future[Either[String, Double]] = {
    parseDouble(inputA).flatMap { eitherA =>
      parseDouble(inputB).flatMap { eitherB =>
        (eitherA, eitherB) match {
          case (Right(a), Right(b)) => divideAync(a, b)
          case (Left(x), _) => Future.successful(Left(x))
          case (_, Left(x)) => Future.successful(Left(x))
        }
      }
    }
  }
  import scala.concurrent.duration._
  implicit val timeOut = Timeout(2.seconds)
  val res = Await.result(divideProgram("12", "2"), timeOut.duration)
  println(res)
}

object Program3 extends App {

  import cats.implicits._
  def parseDoubleAsync(s: String): Future[Either[String, Double]] = {
    Future.successful(Try(s.toDouble).map(Right(_)).getOrElse(Left(s"string $s is not a number")))
  }

  def divide(a: Double, b: Double): Either[String, Double] = {
    Either.cond(b != 0, a / b, "cannot divide by zero")
  }

  def divideAync(a: Double, b: Double): Future[Either[String, Double]] = {
    Future.successful(divide(a, b))
  }

  def divisionProgramAsync(inputA: String, inputB: String): EitherT[Future, String, Double] = {
    for {
      a <- EitherT(parseDoubleAsync(inputA))
      b <- EitherT(parseDoubleAsync(inputB))
      c <- EitherT(divideAync(a, b))
    } yield c
  }

  val f1: EitherT[Future, String, Double] = divisionProgramAsync("12", "2")
  val f2: Future[Either[String, Double]] = divisionProgramAsync("12", "2").value

  f2.onComplete {
    case scala.util.Success(s) => println(s)
    case scala.util.Failure(_) => println("some error")
  }

Thread.sleep(1000)
}