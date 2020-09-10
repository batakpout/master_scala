trait Food { val name: String }
trait Fruit extends Food
trait Cereal extends Food

case class Apple(name: String) extends Fruit
case class Orange(name: String) extends Fruit
case class Muesli(name: String) extends Cereal

case class FoodBowl[+F <: Food](food: F) {
  def eat: String = s"Yummy ${food.name}"
}

val fiji = Apple("fiji")
val jaffa = Orange("jaffa")
val alpen = Muesli("alpen")

val bowlOfAlpen = FoodBowl(alpen)
bowlOfAlpen.eat

val foodInBowl1 = bowlOfAlpen.food

val foodInBowl2: Muesli = bowlOfAlpen.food

// but this doesn't work - try it:
// val foodInBowl: bowlOfAlpen.F = bowlOfAlpen.food