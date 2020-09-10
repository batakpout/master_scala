
trait Thing
trait Vehicle extends Thing
class Car extends Vehicle
class Jeep extends Car
class Coupe extends Car
class Motorcycle extends Vehicle
class Vegetable
class Parking[A](val place: A)

new Parking[Motorcycle](new Motorcycle)
new Parking[Vehicle](new Motorcycle)
//new Parking[Motorcycle](new Car)

class Parking2[A](val place1: A, val place2: A)

new Parking2[Car](new Jeep, new Coupe)
//new Parking2[Car](new Jeep, new Motorcycle)
new Parking2(new Jeep, new Motorcycle) // no type mentioned, so works

class Box[T]

//val x: Box[Car] = new Box[Jeep]()
val x: Car = new Jeep()


