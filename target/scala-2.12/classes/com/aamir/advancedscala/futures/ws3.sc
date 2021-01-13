/*
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
val f = Future(1)

val ff = f.filter(_ == 100).recover {
  case e => println("keeeel" + e)
}
Thread.sleep(10000)é

ff.onComplete{
  case Success(s) => println("success : " + s)
  case Failure(e) => println("Failure: " + e.getMessage)
}*/

val workers = List("a", "b", "c", "d")
workers.flatten
val sentences = List(1,2,3,4,5,6,7,8,9,10)
Iterator.continually(workers).flatten.zip(sentences.iterator).map { pair =>
  val (worker, sentence) = pair
  println(worker)
  println(sentence)
  pair
}

res1.next()

