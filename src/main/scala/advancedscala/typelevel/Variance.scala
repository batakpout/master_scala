package advancedscala.typelevel

object Variance1 extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal


  class CovariantCage[+T](val animal: T)

  val c1: CovariantCage[Animal] = new CovariantCage[Cat](new Cat())
  /*
     class CovariantCage[-T](val animal: T), not allowed because otherwise
      val c1: CovariantCage[Cat] = new CovariantCage[Animal](new Dog())
   */

  //  class CovariantVariableCage[+T](var animal: T) // types of vars are in CONTRAVARIANT POSITION
  /*
    val ccage: CCage[Animal] = new CCage[Cat](new Cat)
    ccage.animal = new Crocodile
   */

  //  class ContravariantVariableCage[-T](var animal: T) // also in COVARIANT POSITION
  /*
    val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)
   */

  class InvariantVariableCage[T](var animal: T)

/*  class AnotherCovariantCage[+T] {
    def addAnimal(animal: T)
  }

  val ccage: AnotherCovariantCage[Animal] = new AnotherCovariantCage[Dog]
  ccage.addAnimal(new Cat())*/

  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true
  }

  // METHOD ARGUMENTS ARE IN CONTRAVARIANT POSITION.
  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  acc.addAnimal(new Cat())
  class Kitty extends Cat
  acc.addAnimal(new Kitty)

  //Allowing covariant Method arguments to work in contravariant position
  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B] // widening the type
  }

  val emptyList: MyList[Kitty] = new MyList[Kitty]
  val animals: MyList[Kitty] = emptyList.add(new Kitty)
  val moreAnimals: MyList[Cat] = animals.add(new Cat)
  val evenMoreAnimals: MyList[Animal] = moreAnimals.add(new Dog) // it will widen Dog to be treated as Animal, and dog is lost.
  //val newList: MyList[Kitty] = evenMoreAnimals.add(new Kitty) CTE
  val newList: MyList[Animal] = evenMoreAnimals.add(new Kitty)

  //Method Return types are in covariant position

/*  class PetShop[-T] {
    def get(isItaPuppy: Boolean): T // METHOD RETURN TYPES ARE IN COVARIANT POSITION
  }

  val catShop: PetShop[Cat] = new PetShop[Animal] {
    def get(isItaPuppy: Boolean): Animal = new Cat
  }

  val dogShop: PetShop[Dog] = catShop
  dogShop.get(true)*/

  trait Car
  class FerrariTemp
  class Ferrari extends FerrariTemp with Car
  class Audi extends Car
  class Cage[+A] {
    def add(x: Boolean):Cage[A] = new Cage[A]
  }


  val cList1: Cage[Ferrari] = new Cage[Ferrari]
   val f1: Cage[Car] = cList1.add(true)

  val l1: List[Cage[Ferrari]] = List(cList1)
  val l2: List[Cage[Car]] = l2
  //l2 += CoList[Audi]

  class XCage[-A] {
    def add(element: A) = true
  }

  val xCage: XCage[Car] = new XCage[Car]
  val l4: List[XCage[Ferrari]] = List(xCage)
  val xCage2: XCage[Ferrari] = new XCage[Ferrari]
  val l5: List[XCage[Ferrari]] = xCage2 :: l4
  val xCage3: XCage[FerrariTemp] = new XCage[FerrariTemp]
  val l6: List[XCage[Ferrari]] = xCage3 :: l5

  val xCage4: XCage[Audi] = new XCage[Audi]
  //val l7: List[XCage[Ferrari]] = xCage4 :: l6


  class PetShop[-T] {

  }
  val catShop: PetShop[Animal] = new PetShop[Animal]
  val dogShop: PetShop[Dog] = catShop


}

