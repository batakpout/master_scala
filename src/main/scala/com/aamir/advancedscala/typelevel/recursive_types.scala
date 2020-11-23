package com.aamir.advancedscala.typelevel


object recursive_types_1 extends App {

   trait Animal {
     def breed: List[Animal]
   }

  trait Cat extends Animal {
    override def breed: List[Cat] = ???
  }

  trait Dog extends Animal {
    override def breed: List[Dog] = ???
    //override def breed: List[Cat] = ???, this is wrong how to avoid this
    // Recursive type or F-bounded Polymorphism to the rescue
  }
}

object recursive_types_2 extends App {

  trait Animal[A <: Animal[A]] {
    def breed: List[Animal[A]]
   }

  trait Cat extends Animal[Cat] {
    override def breed: List[Animal[Cat]] = ???
    //override def breed: List[Animal[Dog]] = ??? CTE, handled now
  }

  trait Crocodile extends Animal[Cat] { // this is bad, name Crocodile check app 3
    override def breed: List[Animal[Cat]] = ???
  }
}

object recursive_types_3 extends App {
  //FBP + self types
  trait Animal[A <: Animal[A]] { self: A =>
    def breed: List[Animal[A]]
  }

  trait Cat extends Animal[Cat] {
    override def breed: List[Animal[Cat]] = ???
    //override def breed: List[Animal[Dog]] = ??? CTE, handled now
  }

 /* trait Crocodile extends Animal[Cat] { // CTE
    override def breed: List[Animal[Cat]] = ???
  }*/
}
//some usages
trait Entity[E <: Entity[E]] //ORM
class Person extends Comparable[Person] { // FBP
  override def compareTo(o: Person): Int = ???
}

//one more issue, its limitation, once we start bring down class inheritance to 1 level, we lose its effectiveness
object recursive_types_4 extends App {

  trait Animal[A <: Animal[A]] { self: A =>
    def breed: List[Animal[A]]
  }
  trait Fish extends Animal[Fish]
  class Cod extends Fish {
        override def breed: List[Animal[Fish]] = ???
  }

  class Shark extends Fish {
    override def breed: List[Animal[Fish]] = List(new Cod)
  }


}

// solution to app 4
object recursive_types_5 extends App {
 //using type classes

  trait Khoonkhar

  trait Animal[A] {
    def breed[A](a: A): List[A]
  }

  class Dog extends Khoonkhar
  object Dog {
    implicit object DogAnimal extends Animal[Dog] {
      override def breed[Dog](a: Dog): List[Dog] = List()
    }
  }

  class Cat extends Khoonkhar
  object Cat {
    implicit object CatAnimal extends Animal[Cat] {
      override def breed[Cat](a: Cat): List[Cat] = List()
    }
  }

  implicit class AnimalOps[A <: Khoonkhar](animal: A) {
    def breed(implicit animalTypeClassInstance: Animal[A]): List[A] =
      animalTypeClassInstance.breed(animal)
  }

  val d = new Dog()
  d.breed

  val c = new Cat()
  c.breed
  //"dd".breed

}