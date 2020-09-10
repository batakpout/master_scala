
/* Copyright (C) 2010-2017 Escalate Software, LLC All rights reserved. */

package koans

import org.scalatest.Matchers
import org.scalatest.SeveredStackTraces
import support.KoanSuite

// please note - due to a quirk of the scala compiler, the commented out code here, while
// technically valid as an example, will not compile (can't existentially abstract
// over parameterized type Car[Int]). This is because of a combination of the multiple
// implementations of Car in this file, including the final top level trait at the bottom
// and the scala compiler having some oddities in by-name functions (which you
// don't typically do). Use the code below for guidance if you need to, but for your
// own implementation, we recommend defining Car at the top level of the Module01 class
// or even at the very top level or in another source file. Your choice.

class Module02Solutions extends KoanSuite with Matchers with SeveredStackTraces {

  // In this exercise you will create a rail car that can carry certain types of
  // items (represented by Scala types).

  // let's start off with a few types
  // A superclass that represents anything that might be transported:
  abstract class TransportableItem {
    def name: String
  }

  // a person can be transported on a train
  case class Person(name: String) extends TransportableItem

  // livestock can also be transported
  abstract class Livestock extends TransportableItem

  // Cows and Pigs are examples of livestock
  case class Pig(name: String) extends Livestock
  case class Cow(name: String) extends Livestock

  // Cargo is some inanimate object that can be transported
  abstract class Cargo extends TransportableItem

  // and for cargo, we will deal exclusively with toothpicks and pianos
  case class Toothpick(name: String) extends Cargo
  case class Piano(name: String) extends Cargo

  // The first test is simply to provide a slick way to create an empty car of a given type

  test("Can create an empty Car of a certain type with Car.empty[TYPE]") {
    class Car[T <: TransportableItem]

    object Car {
      def empty[T <: TransportableItem] = new Car[T]
    }

    Car.empty[Cargo]
    Car.empty[Livestock]

    // note that we should not be able to make a Car for anything that is not a subtype
    // of Transportable item, see the section on upper bounds to remind yourself how to do this

    assertDoesNotCompile("Car.empty[String]")
  }

  // The next test will require that you implement at least part of the functionality
  // of the the put method. This Car is an immutable object, so add returns a new Car, potentially of a more general type:
  //
  // We should also be able to find out how many items are in the car at present, using a
  // numberOfItems method that returns an Int
  //
  // and be able to query for the nth item
  // using an item(n: Int) method that returns an Option[T] which may be none if the requested
  // index is out of bounds.
  //
  // We suggest you hold on to items in a car with a Vector, as this makes updating items at the end
  // and retrieving an item by index easy and efficient.

  test("Empty Car should store items of the right type and access them by index") {
    class Car[T <: TransportableItem](items: Vector[T]) {
      def add(x: T): Car[T] = new Car(items :+ x)
      def numberOfItems: Int = items.size
      def item(n: Int): Option[T] = items.drop(n).headOption
    }
    object Car {
      def empty[T <: TransportableItem] = new Car[T](Vector.empty)
    }

    val emptyCar = Car.empty[Cargo]
    emptyCar.numberOfItems should be (0)
    emptyCar.item(0) should be (None)

    val carWithToothpick = emptyCar.add(Toothpick("toothpick-1"))
    carWithToothpick.numberOfItems should be (1)
    carWithToothpick.item(0) should be (Some(Toothpick("toothpick-1")))
    carWithToothpick.item(1) should be (None)

    val carWithToothpickAndPiano = carWithToothpick.add(Piano("Yamaha"))
    carWithToothpickAndPiano.numberOfItems should be (2)
    carWithToothpickAndPiano.item(0) should be (Some(Toothpick("toothpick-1")))
    carWithToothpickAndPiano.item(1) should be (Some(Piano("Yamaha")))
    carWithToothpickAndPiano.item(2) should be (None)

    // but without correct variance, this won't compile right now - check it and see
    assertDoesNotCompile(
      "val anyCar: Car[TransportableItem] = carWithToothpickAndPiano"
    ) // doesn't work because Car is invariant in its type parameter

    // you also cannot add a Pig to a cargo car (invariance)
    assertDoesNotCompile(
      """val mixedCar = carWithToothpickAndPiano.add(Pig("Percival"))"""
    )
  }

  // Now see if you can make the Car covariant in its type parameter, so that
  // someone could pass a Car[Cargo] to a method expecting a Car[TransportableItem].
  // Note that in order for the tests to pass, you still need to ensure that
  // only TransportableItem subtypes can be put in a Car (this will need to
  // be enforced on any new generics you introduce as well)

  test("Car should be covariant in its type parameter") {
    class Car[+T <: TransportableItem](items: Vector[T]) {
      def add[U >: T <: TransportableItem](x: U): Car[U] = new Car(items :+ x)
      def numberOfItems: Int = items.size
      def item(n: Int): Option[T] = items.drop(n).headOption
    }
    object Car {
      def empty[T <: TransportableItem] = new Car[T](Vector.empty)
    }

    val emptyCar = Car.empty[Cargo]
    emptyCar.numberOfItems should be (0)
    emptyCar.item(0) should be (None)

    val carWithToothpick: Car[Cargo] = emptyCar.add(Toothpick("toothpick-1"))
    carWithToothpick.numberOfItems should be (1)
    carWithToothpick.item(0) should be (Some(Toothpick("toothpick-1")))
    carWithToothpick.item(1) should be (None)

    val carWithToothpickAndPiano: Car[Cargo] = carWithToothpick.add(Piano("Yamaha"))
    carWithToothpickAndPiano.numberOfItems should be (2)
    carWithToothpickAndPiano.item(0) should be (Some(Toothpick("toothpick-1")))
    carWithToothpickAndPiano.item(1) should be (Some(Piano("Yamaha")))
    carWithToothpickAndPiano.item(2) should be (None)

    val anyCar: Car[TransportableItem] = carWithToothpickAndPiano

    assertDoesNotCompile("val toothpick1: Cargo = anyCar.item(0).get")  // because we widened the type param
    assertCompiles("val toothpick1: TransportableItem = anyCar.item(0).get")
    val toothpick: TransportableItem = anyCar.item(0).get

    toothpick should be (Toothpick("toothpick-1"))

    // we can also now add a Pig to a cargo car, this will automatically generalize the type
    val anyCar2: Car[TransportableItem] = carWithToothpickAndPiano.add(Pig("Percival"))
    // this compiles, but what is the type of anyCar2 now?

    anyCar2.item(2).get should be (Pig("Percival"))

    assertDoesNotCompile("""val anyCar3 = anyCar2.add("Hello World, I am a string")""")
  }

  // Extra credit - refactor your Car so that the only two public parts are a
  // completely abstract trait defining the Car interface,
  // and a companion object that contains the implementation class as a private
  // member as well as an empty method that creates an empty Car instance.

  test("Car should work as expected with an abstract trait separated out for its API") {

    object Car {
      private class CarImpl[+T <: TransportableItem](items: Vector[T]) extends Car[T] {
        def add[U >: T <: TransportableItem](x: U): Car[U] = new CarImpl(items :+ x)
        def numberOfItems: Int = items.size
        def item(n: Int): Option[T] = items.drop(n).headOption
      }
      def empty[T <: TransportableItem]: Car[T] = new CarImpl[T](Vector.empty)
    }

    trait Car[+T <: TransportableItem] {
      def add[U >: T <: TransportableItem](x: U): Car[U]
      def numberOfItems: Int
      def item(n: Int): Option[T]
    }

    val emptyCar = Car.empty[Cargo]
    emptyCar.numberOfItems should be (0)
    emptyCar.item(0) should be (None)

    val carWithToothpick: Car[Cargo] = emptyCar.add(Toothpick("toothpick-1"))
    carWithToothpick.numberOfItems should be (1)
    carWithToothpick.item(0) should be (Some(Toothpick("toothpick-1")))
    carWithToothpick.item(1) should be (None)

    val carWithToothpickAndPiano: Car[Cargo] = carWithToothpick.add(Piano("Yamaha"))
    carWithToothpickAndPiano.numberOfItems should be (2)
    carWithToothpickAndPiano.item(0) should be (Some(Toothpick("toothpick-1")))
    carWithToothpickAndPiano.item(1) should be (Some(Piano("Yamaha")))
    carWithToothpickAndPiano.item(2) should be (None)

    val anyCar: Car[TransportableItem] = carWithToothpickAndPiano

    assertDoesNotCompile("val toothpick1: Cargo = anyCar.item(0).get")  // because we widened the type param
    assertCompiles("val toothpick1: TransportableItem = anyCar.item(0).get")
    val toothpick: TransportableItem = anyCar.item(0).get

    toothpick should be (Toothpick("toothpick-1"))

    // still should not be able to put non TransportableItems into a car
    assertDoesNotCompile("""val anyCar3 = anyCar.add("Hello World, I am a string")""")
  }

  test("A Dock should be able to handle all cargo of a more specific type than it is defined on") {
    // in this example, Ship's type param is invariant, the point is to *not* change the variance of Ship, but instead
    // to change the variance of the Dock definition below it!
    case class Ship[T <: TransportableItem](items: Seq[T])

    class Dock[-T <: TransportableItem] {
      def unload[U <: T](ship: Ship[U]): Seq[U] = ship.items
    }

    // if we have a Dock for toothpicks, it should unload a ship loaded with toothpicks
    val toothpickDock = new Dock[Toothpick]
    val toothpickShip: Ship[Toothpick] = Ship(Seq(Toothpick("tp1"), Toothpick("tp2")))
    toothpickDock.unload(toothpickShip) should be (toothpickShip.items)

    // now for a trickier one, you will need to alter the above Dock definition to make this work
    // A livestock dock should be able to unload any livestock, be it pigs or cows or a mix
    val livestockDock = new Dock[Livestock]
    val pigShip: Ship[Pig] = Ship(Seq(Pig("Porky"), Pig("Perkins")))
    val cowShip: Ship[Cow] = Ship(Seq(Cow("Bessie"), Cow("Daisy")))
    val mixedLivestockShip: Ship[Livestock] = Ship(Seq(Pig("Porky"), Pig("Perkins"), Cow("Bessie"), Cow("Daisy")))

    livestockDock.unload(mixedLivestockShip) should be (mixedLivestockShip.items)  // that's the easy one, but what about the others?

    // Uncomment these two tests and then fix the variance and bounds of Dock to make this work
    livestockDock.unload(pigShip) should be (pigShip.items)
    livestockDock.unload(cowShip) should be (cowShip.items)

    // and that means a dock for any transportable item should handle anything:
    // Note how the returned sequences are the same type as the ship even though the dock type is more generic, neat huh?
    val transportableItemDock = new Dock[TransportableItem]
    val toothpicksReceived: Seq[Toothpick] = transportableItemDock.unload(toothpickShip)
    val livestockReceived: Seq[Livestock] = transportableItemDock.unload(mixedLivestockShip)
    val pigsReceived: Seq[Pig] = transportableItemDock.unload(pigShip)

    toothpicksReceived should be (toothpickShip.items)
    livestockReceived should be (mixedLivestockShip.items)
    pigsReceived should be (pigShip.items)
  }

}
