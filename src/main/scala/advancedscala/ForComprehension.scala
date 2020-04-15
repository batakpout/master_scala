sealed abstract class Perhaps[+A] {
  def foreach(f: A => Unit): Unit

  def map[B](f: A => B): Perhaps[B]

  def flatMap[B](f: A => Perhaps[B]): Perhaps[B]

  def withFilter(f: A => Boolean): Perhaps[A]
}

case class YesItIs[A](value: A) extends Perhaps[A] {
  override def foreach(f: A => Unit): Unit = f(value)

  override def map[B](f: A => B): Perhaps[B] = YesItIs(f(value))

  override def flatMap[B](f: A => Perhaps[B]): Perhaps[B] = f(value)

  override def withFilter(f: A => Boolean): Perhaps[A] = if (f(value)) this else Nope
}

case object Nope extends Perhaps[Nothing] {
  override def foreach(f: Nothing => Unit): Unit = ()

  override def map[B](f: Nothing => B): Perhaps[B] = this

  override def flatMap[B](f: Nothing => Perhaps[B]): Perhaps[B] = this

  override def withFilter(f: Nothing => Boolean): Perhaps[Nothing] = this
}

object ForComprehension1 extends App {


  val y3 = YesItIs(3)
  val y4 = YesItIs(4)
  val n = Nope

  // foreach:
  for {
    a <- y3
  } println(a)

  y3.foreach(a => println(a))

  // map:
  for {
    a <- y3
  } yield a * a

  y3.map(a => a * a)

  // flatMap/map:
  for {
    a <- y3
    b <- y4
  } yield a * b

  y3.flatMap(a => y4.map(b => a * b))

  // combining Perhaps with Try:

  import scala.util.Try

  for {
    a <- y4
  } yield for {
    b <- Try(100 / a)
  } yield s"100/$a=$b" // YesItIs(Success(100/4=25))

  y4.map { a =>
    Try(100 / a).map { b =>
      s"100/$a=$b"
    }
  }

  // withFilter:
  for {
    a <- y3
    if a > 1
    b <- y4
  } yield a * b

  y3.withFilter(a => a > 1).flatMap(a => y4.map(b => a * b))

  // assignment:
  for {
    a <- y3
    b <- y4
    c = a * b
  } yield c

  y3.flatMap { a =>
    y4.map { b =>
      val c = a * b
      c
    }
  }
}