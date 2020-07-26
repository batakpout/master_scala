import org.scalatest.FunSpec
import org.scalatest.mockito.MockitoSugar
object Monad1  {
//so attempt monad will abstract away computation which might fail or not
  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] //bind
  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] = {
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

class MonadTest extends FunSpec with MockitoSugar  {

 import Monad1._
  describe("monad laws") {
    val x = "monad"
    val monad: Attempt[String] = Success(x)
    val f: String => Attempt[String] = (value: String) => Attempt(s"hello $value")
    val g: String => Attempt[String] = (value: String) => Attempt(s"$value!")

    val l: Attempt[String] = monad.flatMap(f).flatMap(g)
    val m: Attempt[String] = monad.flatMap(x => f(x).flatMap(g))

    it("should satisfy left-identity law") {
      assert(monad.flatMap(f) == f(x))
    }
    it("should satisfy right-identity law") {
      assert(monad.flatMap(Success(_)) == monad)
    }
    it("should satisfy associativity law") {
      assert(monad.flatMap(f).flatMap(g) == monad.flatMap(x => f(x).flatMap(g)))
    }

  }
}