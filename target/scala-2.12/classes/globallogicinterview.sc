
import scala.concurrent.Future
import scala.concurrent.Future
import scala.util.{Failure,Success, Try}

val m1 = Map[String, Int]("a" -> 1, "b" -> 3, "d" -> 4).toList

val m2 = Map[String, Int]("a" -> 7, "c" -> 3, "d" -> 1).toList

val x = (m1 ++ m2).groupBy(_._1).map { case (key, value) =>
  key -> value.map(_._2).sum
}

val xx: Try[Int] = Try(10)

val res: Future[Int] = xx match {
  case Success(s) => Future.successful(s)
  case Failure(e) => Future.failed(e)
}




