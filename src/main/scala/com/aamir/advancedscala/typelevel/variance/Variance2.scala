package com.aamir.advancedscala.typelevel.variance

object Test extends App {
  /*
  class Box[-T] {
    def get(x: T):T = ???
  }*/
}

object Variance2 extends App {

  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle
  class IList[T]

  class IParking[T](vehicles: List[T]) {
    def park(vehicle: T): IParking[T] = ???
    def impound(vehicles: List[T]): IParking[T] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }

  class CParking[+T](vehicles: List[T]) {
    def park[S >: T](vehicle: S): CParking[S] = ???
    def impound[S >: T](vehicles: List[S]): CParking[S] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => CParking[S]): CParking[S] = ???
  }

  class XParking[-T](vehicles: List[T]) {
    def park(vehicle: T): XParking[T] = ???
    def impound(vehicles: List[T]): XParking[T] = ???
    def checkVehicles[S <: T](conditions: String): List[S] = ???

    def flatMap[R <: T, S](f: Function1[R, XParking[S]]): XParking[S] = ???
  }

  /*
    Rule of thumb
    - use covariance = COLLECTION OF THINGS
    - use contravariance = GROUP OF ACTIONS
   */

  class CParking2[+T](vehicles: IList[T]) {
    def park[S >: T](vehicle: S): CParking2[S] = ???
    def impound[S >: T](vehicles: IList[S]): CParking2[S] = ???
    def checkVehicles[S >: T](conditions: String): IList[S] = ???
  }

  class XParking2[-T](vehicles: IList[T]) {
    def park(vehicle: T): XParking2[T] = ???
    def impound[S <: T](vehicles: IList[S]): XParking2[S] = ???
    def checkVehicles[S <: T](conditions: String): IList[S] = ???
  }
}
object Variance3 extends App {

  class Vehicle

  class Bike extends Vehicle

  class Car extends Vehicle

  class IList[T]


  class IParking[T](vehicle: List[T]) {
    def park(vehicle: T): IParking[T] = ???

    def impound(vehicles: List[T]): IParking[T] = ???

    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }

  class CTList[-T]

  class CParking[+T](vehicles: CTList[T]) {

    def park[S >: T](vehicle: S): CParking[S] = ???

    def impound(vehicles: CTList[T]): CParking[T] = ???

    def checkVehicles[S >: T](conditions: String): CTList[S] = ???

    def flatMap[S](f: Function1[T, S]): CParking[S] = ???

  }


  class XParking[-T](vehicles: CTList[T]) {
    def park(vehicle: T): CTList[T] = ???

    def impound[S <: T](vehicles: CTList[S]): XParking[S] = ???

    def checkVehicles(conditions: String): CTList[T] = ???

    def flatMap[R <: T, S](f: Function1[R, IParking[S]]): IParking[S] = ???

  }

}

