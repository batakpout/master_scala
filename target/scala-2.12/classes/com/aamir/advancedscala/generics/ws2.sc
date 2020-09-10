
//https://www.journaldev.com/9609/scala-typebounds-upper-lower-and-view-bounds
//http://blog.kamkor.me/Covariance-And-Contravariance-In-Scala/
//https://blog.codecentric.de/en/2015/04/the-scala-type-system-parameterized-types-and-variances-part-3/
//https://dzone.com/users/2953971/rruizginer.html
class Animal[+T](val animal:T)

class Wild
class Dog extends Wild
class Puppy extends Dog

class Crock extends Wild

    class AnimalCarer(val dog:Animal[Dog])

    val puppy = new Puppy
    val dog = new Dog
    val crock = new Crock

    val puppyAnimal:Animal[Puppy] = new Animal[Puppy](puppy)
    val dogAnimal:Animal[Dog] = new Animal[Dog](dog)


    val crockAnimal :Animal[Crock] = new Animal[Crock](crock)

    val dogCarer = new AnimalCarer(dogAnimal)
    val puppyCarer = new AnimalCarer(puppyAnimal)
    //val crockCarer = new AnimalCarer(crockAnimal)

    println("Done.")

abstract class Type [-T]{
  def typeName : Unit
}

class SuperType extends Type[AnyVal]{
  override def typeName: Unit = {
    println("SuperType")
  }
}
class SubType extends Type[Int]{
  override def typeName: Unit = {
    println("SubType")
  }
}

class TypeCarer{
  def display(t: Type[Int]){
    t.typeName
  }
}


    val superType = new SuperType
    val subType = new SubType

    val typeCarer = new TypeCarer

    typeCarer.display(subType)
    typeCarer.display(superType)