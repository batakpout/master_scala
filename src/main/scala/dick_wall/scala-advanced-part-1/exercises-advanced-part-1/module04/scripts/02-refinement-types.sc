trait Food { val name: String }
trait Fruit extends Food
trait Cereal extends Food

case class Apple(name: String) extends Fruit
case class Orange(name: String) extends Fruit
case class Muesli(name: String) extends Cereal

val fiji = Apple("Fiji")
val alpen = Muesli("Alpen")

case class FoodBowlT[+F <: Food](item: F)
val appleBowl = FoodBowlT[Apple](fiji)
val muesliBowl = FoodBowlT[Muesli](alpen)

def feedToFruitEater(bowl: FoodBowlT[Fruit]) =
  println(s"Yummy ${bowl.item.name}")

feedToFruitEater(appleBowl)

// Can't do it Jim, I don't have the types...
// feedToFruitEater(muesliBowl)

abstract class FoodBowl {
  type FOOD <: Food
  val item: FOOD
}
class AppleBowl extends FoodBowl {
  type FOOD = Apple
  val item = fiji
}
class MuesliBowl extends FoodBowl {
  type FOOD = Muesli
  val item = alpen
}
def feedToFruitEater(bowl: FoodBowl) = println(s"yummy ${bowl.item.name}")

feedToFruitEater(new AppleBowl)

feedToFruitEater(new MuesliBowl)  // oops - that shouldn't have worked

def safeFeedToFruitEater(bowl: FoodBowl { type FOOD <: Fruit }) =
  println(s"yummy ${bowl.item.name}")
safeFeedToFruitEater(new AppleBowl)

// and now this will no longer compile
//safeFeedToFruitEater(new MuesliBowl)