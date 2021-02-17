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

class ListMergeTest extends FunSpec with MockitoSugar  {

  describe("list combine test") {


    val l1 = List(1,2,4, 6, 100)
    val l2 = List(5, 200)
    val result =  List(1, 2, 4, 5, 6, 100, 200)


    it("should merge the two list and sort them") {

      assert(ListCombine.combine(l1, l2) == result)
    }


  }
}