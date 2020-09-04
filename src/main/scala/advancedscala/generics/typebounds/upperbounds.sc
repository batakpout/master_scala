class Animal
class Dog extends Animal
class Puppy extends Dog

class AnimalCarer{
  def display [T <: Dog](t: T){
    println(t)
  }
}

    val animal = new Animal
    val dog = new Dog
    val puppy = new Puppy

    val animalCarer = new AnimalCarer

    //animalCarer.display(animal)
    animalCarer.display(dog)
    animalCarer.display(puppy)
