abstract class Food { val name: String }

abstract class Fruit extends Food
case class Banana(name: String) extends Fruit
case class Apple(name: String) extends Fruit

abstract class Cereal extends Food
case class Granola(name: String) extends Cereal
case class Muesli(name: String) extends Cereal

val fuji = Apple("Fuji")
val alpen = Muesli("Alpen")

def eat(f: Food): String = s"${f.name} eaten"

eat(fuji)

eat(alpen)

case class Bowl(food: Food) {
  override def toString = s"A bowl of yummy ${food.name}s"
  def contents = food
}
val fruitBowl = Bowl(fuji)
val cerealBowl = Bowl(alpen)
fruitBowl.contents
cerealBowl.contents

case class Bowl2[F](contents: F) {
  override def toString: String = s"A yummy bowl of ${contents}s"
}

val appleBowl = Bowl(fuji)
val muesliBowl = Bowl(alpen)
appleBowl.contents
muesliBowl.contents

// but this won't work
//case class Bowl3[F](contents: F) {
//  override def toString: String = s"A yummy bowl of ${contents.name}s"
//}
