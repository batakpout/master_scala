package com.aamir.advancedscala.typelevel

/** In Layman's term,
 * A deeper generic type with some unknown type parameter at some deepest level.
 */
object  HigherKindedTypes1 extends App {

   trait AHigherKindedType[F[_]]

  trait MyList[T] {
    def flatMap[B](f: T => List[B]): MyList[B]
  }

  trait MyOption[T] {
    def flatMap[B](f: T => MyOption[B]): MyOption[B]
  }

  trait MyFuture[T] {
    def flatMap[B](f: T => MyFuture[B]): MyFuture[B]
  }



  // for a generic multiply method using HKT
  trait Monad[F[_], A] {
    def flatMap[B](f: A => F[B]): F[B]
    def map[B](f: A => B): F[B]
  }

  class MonadList[A](list: List[A]) extends Monad[List, A] {
    override def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)
    override def map[B](f: A => B): List[B] = list.map(f)
  }

  def multiply[F[_], A, B](mA: Monad[F, A], mB: Monad[F, B]): F[(A, B)] = {
    for {
      a <- mA
      b <- mB
    } yield (a, b)
  }

  val monadList = new MonadList(List(1,3))
  monadList.flatMap(x => List(x, x + 1)) // from Monad[List, Int] we return List[Int]
  monadList.map(_ * 2) //// from Monad[List, Int] we return List[Int]

 println {
   multiply(new MonadList(List(1, 2)), new MonadList(List('a', 'b')))
 }

}

object  HigherKindedTypes2 extends App {

  trait Monad[F[_], A] {
    def flatMap[B](f: A => F[B]): F[B]
    def map[B](f: A => B): F[B]
  }

  class MonadList[A](list: List[A]) extends Monad[List, A] {
    override def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)
    override def map[B](f: A => B): List[B] = list.map(f)
  }

  class MonadOption[A](option: Option[A]) extends Monad[Option, A] {
    override def flatMap[B](f: A => Option[B]): Option[B] = option.flatMap(f)
    override def map[B](f: A => B): Option[B] = option.map(f)
  }

  def multiply[F[_], A, B](mA: Monad[F, A], mB: Monad[F, B]): F[(A, B)] = {
    for {
      a <- mA
      b <- mB
    } yield (a, b)
  }


  println {
    multiply(new MonadList(List(1, 2)), new MonadList(List('a', 'b')))
  }

  println {
    multiply[Option, Int, String](new MonadOption[Int](Some(2)), new MonadOption[String](Some("a")))
  }

}

//with implicits
object  HigherKindedTypes3 extends App {

  trait Monad[F[_], A] { //Higher kinded type class.
    def flatMap[B](f: A => F[B]): F[B]
    def map[B](f: A => B): F[B]
  }

  implicit class MonadList[A](list: List[A]) extends Monad[List, A] {
    override def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)
    override def map[B](f: A => B): List[B] = list.map(f)
  }

  implicit class MonadOption[A](option: Option[A]) extends Monad[Option, A] {
    override def flatMap[B](f: A => Option[B]): Option[B] = option.flatMap(f)
    override def map[B](f: A => B): Option[B] = option.map(f)
  }

   def multiply[F[_], A, B](mA: Monad[F, A], mB: Monad[F, B]): F[(A, B)] = {
    for {
      a <- mA
      b <- mB
    } yield (a, b)
  }


  println {
    multiply(List(1, 2), List('a', 'b'))
  }

  println {
    multiply[Option, Int, String](Some(2), Some("a"))
  }

}