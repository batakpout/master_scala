import scala.concurrent.Future

trait Animal {
  def eat(): Unit
  def name = "animal"
}

val x: Animal = () => println("Eat")
x.name
x.eat()

new Thread(
  () => println("")
)

class Mutable {
  private var internalMember: Int = 0 // private for OO encapsulation
  def member = internalMember // "getter"
  def member_=(value: Int): Unit = internalMember = value // "setter"
}

val o = new Mutable()
o.member
o.member = 10
o.member

val s: Seq[Int] = Seq(1,2,3,4)
val x: Seq[Int] = s :+ 33
val y = x :+ 331

val pf:PartialFunction[Int, String] = {
  case 1 => "abc"
}
pf.lift
import scala.concurrent.ExecutionContext.Implicits.global


Set(1,3,3) + 4


