package com.aamir.basicscala.oops


object Inheritance extends App {

  // single class inheritance
  sealed class Animal {
    def creatureType = "wild" // parameterless methods can be overridden by vals in sub classes.
    protected def eat = println("nomnom")
  }

  //
   class Cat extends Animal {
    def crunch = {
      eat
      println("crunch crunch")
    }
  }

  val cat = new Cat
  //cat.eat protected
  cat.crunch


  // constructors
  class Person(name: String, age: Int) {
    def this(name: String) = this(name, 0)
  }
  class Adult(name: String, age: Int, idCard: String) extends Person(name)

  // overriding
  class Dog(override val creatureType: String) extends Animal {
    //    override val creatureType = "domestic"
    override def eat = {
      super.eat
      println("crunch, crunch")
    }
  }
  val dog = new Dog("K9")
  dog.eat
  println(dog.creatureType)


  // type substitution (broad: polymorphism)
  val unknownAnimal: Animal = new Dog("K9")
  //unknownAnimal.eat

  // overRIDING vs overLOADING

  // super

  // preventing overrides
  // 1 - use final on member
  // 2 - use final on the entire class
  // 3 - seal the class = extend classes in THIS FILE, prevent extension in other files
}


/**
 * final members can't be overriden but can be inherited
 */
object Inheritance2 extends App {

  trait Animal {
    def name: String
    protected def sound: String
    final def talk(): Unit = println(s"$name says $sound")
  }

  case class Dog(override val name: String) extends Animal {
    override protected final lazy val sound = "woof"
  }

  case class Cat(override val name: String) extends Animal {
    override protected final lazy val sound = "meow"
  }

  case class Bird(override val name: String) extends Animal {
    override protected final lazy val sound = "chirp"
  }

  val cat: Cat = Cat("Kitty")
  val dog: Dog = Dog("Snuffles")
  val bird: Bird = Bird("Coco")

  val myAnimals: List[Animal] = List(cat, dog, bird)

  myAnimals.foreach(a => a.talk())

  assert(myAnimals.map(a => a.name) == List("Kitty", "Snuffles", "Coco"))
}
