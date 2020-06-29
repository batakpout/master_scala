object Program1 extends App {

  class Animal(name: String)
  class Dog(name: String) extends Animal(name)

  val an: Animal = new Dog("labradog") // normal sub-typing (Polymorphism)

  class GenericType[+T](a: T)



  val a1 = new Animal("")
  val d1 = new Dog("")

  var c1: GenericType[Animal] = new GenericType[Animal](a1)

  val c2: GenericType[Dog] = new GenericType[Dog](d1)


  val z:GenericType[Animal] = new GenericType[Animal](a1)

  val zz:GenericType[Animal] = c2

  def m1(x :GenericType[Animal]) = {

  }

  m1(c1)
  m1(c2)

  // if Dog <: Animal

  //then

  //GenericType[Dog] <: Field[Animal]

}