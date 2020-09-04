class  Animal
class Dog extends Animal
class Puppy extends Animal

class AnimalCarer{
  def display [T >: Puppy](t: T){
    println(t)
  }
}


    val animal = new Animal
    val dog = new Dog
    val puppy = new Puppy

    val animalCarer = new AnimalCarer

    animalCarer.display(animal)
    animalCarer.display(puppy)
    animalCarer.display(dog)



trait Thing
class Vehicle extends Thing
class Car extends Vehicle
class Jeep extends Car
class Coupe extends Car
class Motorcycle extends Vehicle
class Bicycle extends Vehicle
class Tricycle extends Bicycle
//Can we limit Parking to all the subtypes of Vehicles above Tricycle?
class Parking[A >: Bicycle <: Vehicle](val plaza: A)

new Parking(new Bicycle)
new Parking(new Coupe)

//new Parking(new AnyRef)
new Parking(new Tricycle) //the type of Parking is now Parking[Bicycle]

