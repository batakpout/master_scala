val f: Int => Boolean = (x: Int) => x < 5

val l = (1 to 10).toList.groupBy(f)

val donuts: Seq[String] = Seq("Plain Donut", "Strawberry Donut", "Glazed Donut", "Pineapple Donut")

val m = collection.mutable.Map()

donuts.groupBy(_.charAt(0))


def testOptionalImplicit(implicit x: String = "") = {
  s"hi, $x"
}



class Animal()  {

  def walk() = "animal walk"

}

class Cat() extends Animal() {

  override def walk() = "cat walk"

}

class Dog() extends Animal() {

  override def walk() = "dog walk"

}

class Puppy() extends Dog() {

  override def walk() = "puppy walk"

}



class Cage[-T](value: T)

val c:Cage[Cat] = new Cage[Animal](new Dog())