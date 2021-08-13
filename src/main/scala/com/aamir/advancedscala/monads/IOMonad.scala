package com.aamir.advancedscala.monads

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

/**
 * Why Future is not a Monad and can't be a suitable type for encapsulating effects.
 * https://medium.com/@yuriigorbylov/is-future-a-monad-d7e4e07ddd82
 */

object IOMonadA extends App {

  val future = Future(10*10)

  val f1: Future[(Int, Int)] = for {
    res1 <- future
    res2 <- future
  } yield res1 -> res2

  val f2 = for {
    res1 <- Future(10*10)
    res2 <- Future(10*10)
  } yield res1 -> res2

  f1.onComplete(println) // Success((100,100))
  f2.onComplete(println) // Success((100,100))

  Thread.sleep(10000)

}

object IOMonadB extends App {

  val future = Future(Random.nextInt(100))

  val f1 = for {
    res1 <- future
    res2 <- future
  } yield res1 -> res2

  val f2 = for {
    res1 <- Future(Random.nextInt(100))
    res2 <- Future(Random.nextInt(100))
  } yield res1 -> res2

  f1.onComplete(println) // Success((14,14))
  Thread.sleep(2000)
  f2.onComplete(println) // Success((74,42))

   Thread.sleep(5000)
}

object IOMonad1 extends App {

   val result: Future[Unit] = for {
     _ <- Future(println("my precious"))
     _ <- Future(println("my precious"))
   } yield ()

  result.map(x => ())

  Thread.sleep(10000)
}

object IOMonad2 extends App {

   val future = Future(println("my precious"))
  val result: Future[Unit] = for {
    _ <- future
    _ <-future
  } yield ()

  result.map(x => ())

  Thread.sleep(10000)
}

