abstract class Food { val name: String }

abstract class Fruit extends Food
case class Banana(name: String) extends Fruit
case class Apple(name: String) extends Fruit
case class Orange(name: String) extends Fruit

abstract class Cereal extends Food
case class Granola(name: String) extends Cereal
case class Muesli(name: String) extends Cereal

val fuji = Apple("Fuji")
val alpen = Muesli("Alpen")


trait Description {
  val describe: String
}
case class Taste(describe: String) extends Description
case class Texture(describe: String) extends Description

def describeAnApple(fn: Apple => Description) = fn(Apple("Fuji"))
val juicyFruit: Fruit => Taste =
  fruit => Taste(s"This ${fruit.name} is nice and juicy")
describeAnApple(juicyFruit)

val bumpyOrange: Orange => Texture =
  orange => Texture(s"This ${orange.name} is bumpy")

def describeAFruit(fn: Fruit => Description) = fn(Apple("Fuji"))
describeAFruit(juicyFruit)

// describeAFruit(bumpyOrange)

