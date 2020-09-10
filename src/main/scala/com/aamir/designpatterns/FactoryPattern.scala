package designpatterns

object FactoryPattern1 extends App {

  //https://www.geeksforgeeks.org/the-factory-pattern-in-scala/

  abstract class Car {
    def bookingPrice: Double

    def Brands: List[String]

    def availability: Int

    def book(noOfCars: Int)
  }

  object Car {

    val STANDARD = 0
    val DELUXE = 1
    val LUXURY = 2

    private class StandardCar extends Car {
      private var _availability = 100

      override def bookingPrice = 200000

      override def Brands = List("Maruti", "Tata", "Hyundai")

      override def availability = _availability

      override def book(noOfCars: Int) = {
        _availability = _availability - noOfCars
      }
    }

    private class DeluxeCar extends Car {
      private var _availability = 50

      override def bookingPrice = 500000

      override def Brands = List("Honda", "Mahindra", "Chevrolet")

      override def availability = _availability

      override def book(noOfCars: Int) = {
        _availability = _availability - noOfCars
      }
    }

    private class LuxuryCar extends Car {
      private var _availability = 5

      override def bookingPrice = 900000

      override def Brands = List("Audi", "BMW", "Mercedes")

      override def availability = _availability

      override def book(noOfCars: Int) = {
        _availability = _availability - noOfCars
      }
    }

    // single method to create a variety of objects
    def apply(carType: Int): Car = {
      carType match {
        case LUXURY   => new LuxuryCar()
        case DELUXE   => new DeluxeCar()
        case STANDARD => new StandardCar()
        case _        => new StandardCar()
      }
    }
  }

  import Car._

  val s = Car(STANDARD)
  println(s.bookingPrice)
  println(s.availability)

}