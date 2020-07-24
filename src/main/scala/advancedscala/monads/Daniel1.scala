package advancedscala.monads

object Monad1  {

  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] //bind
  }

  object Attempt {
    def unit[A](a: => A): Attempt[A] = {
      try {
        Success(a)
      } catch {
        case e => Fail(e)
      }
    } //pure or apply
  }

  case class Success[A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] = {
      try {
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }
    }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

}

//lazy monad abstracts away a computation which will only be executed when its needed.
//monad = unit + flatMap
//monad = unit + map + flatten

object Monad2 extends App {
  class Lazy[+A](value: => A) {
    def flatMap[B](f: A => Lazy[B]):Lazy[B] = f(value)
  }
  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy[A](value)
  }

  val lazyInstance: Lazy[Int] = Lazy {
    println("I am so lazy to do anything....")
    42
  }

  val flatMapInstance:Lazy[Int] = lazyInstance.flatMap { x => Lazy {
    10 * x
  }}

}

object test extends App {

  def m(value: => Int, f: ( => Int) => String) = {
    lazy val x = value
     f(x)
  }

  lazy val x = {+
    println("Inside value")
    40
  }
    m(x, x => x.toString)

}