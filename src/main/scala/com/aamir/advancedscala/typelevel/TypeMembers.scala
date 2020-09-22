package com.aamir.advancedscala.typelevel

import com.aamir.advancedscala.typelevel.TypeMembers1.ac

/**
   As you know, a class can have field members and method members. Well, Scala also allows a class to have type members.
 */
object TypeMembers1 extends App {
   class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollection {
    type AnimalType // abstract type member
    type BoundedAnimal <: Animal
    type SuperBoundedAnimal >: Dog <: Animal
    type AnimalC = Cat
  }

  val ac = new AnimalCollection
  //val dog: ac.AnimalType = ???

  //val cat: ac.BoundedAnimal = new Cat
  val dog: ac.SuperBoundedAnimal = new Dog
  val cat: ac.AnimalC = new Cat

  type CatAlias = Cat
  val anotherCat: CatAlias = new Cat

 // alternative to generics

  trait MyList {
    type T
    def add(element: T): MyList
  }

  class NonEmptyList(value: Int) extends MyList {
    override type T = Int

    override def add(element: Int): MyList = ???
  }

  // .type
  type CatsType = cat.type
  val newCat: CatsType = cat

  /**
       enforce a type to be applicable to SOME TYPES only
 */

  trait MyCoolList {
    type A
    def head: A
    def tail: MyCoolList
  }
  //type members and type member constraints (bounds)
  trait ApplicableToNumbers {
    type A <: Number
  }

  trait NList extends MyCoolList {
    type S = Int with Double with Float with Byte with Long
    override type A >: S
    //override type A >: Int with Double with Float with Byte with Long

  }

/*   class CustomList(hd: String, tl: CustomList) extends MyCoolList with NList {
     type A = String
     def head = hd
     def tail = tl

   }*/


  class IntList(hd: Int, tl: IntList) extends MyCoolList with NList {
    type A =  Int
    def head = hd
    def tail = tl

  }
}