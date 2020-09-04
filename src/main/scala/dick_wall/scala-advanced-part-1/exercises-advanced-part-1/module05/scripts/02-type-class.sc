import scala.annotation.implicitNotFound

@implicitNotFound("You need to define a CompareT for ${T}")
abstract class CompareT[T] {
  def isSmaller(i1: T, i2: T): Boolean
  def isLarger(i1: T, i2: T): Boolean
}

def genInsert[T: Ordering](item: T, rest: List[T]): List[T] = {
  val cmp = implicitly[Ordering[T]]
  rest match {
    case Nil => List(item)
    case head :: _ if cmp.lt(item, head) => item :: rest
    case head :: tail => head :: genInsert(item, tail)
  }
}

def genSort[T: Ordering](xs: List[T]): List[T] = xs match {
  case Nil => Nil
  case head :: tail => genInsert(head, genSort(tail))
}

val nums = List(1,4,3,2,6,5)

genSort(nums)

case class Person(first: String, age: Int)

object Person {
  implicit object PersonOrdering extends Ordering[Person] {
    override def compare(x: Person, y: Person): Int = x.age - y.age
  }
}

implicit object POrdering2 extends Ordering[Person] {
  override def compare(x: Person, y: Person) = 0
}

val people = List(
  Person("Fred", 25),
  Person("Sally", 22),
  Person("George", 53)
)

genSort(people)
