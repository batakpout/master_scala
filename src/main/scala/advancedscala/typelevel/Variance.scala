package advancedscala.typelevel

object Variance1 extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal


  class CovariantCage[+T](val animal: T)

  val c1: CovariantCage[Animal] = new CovariantCage[Animal](new Cat())

}