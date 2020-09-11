package newbie_training

object OOPS1 extends App {


  class Person {
       private var age: Int = 20

       def getAge: Int = {
         age
       }

      def setAge(newAge: Int) = {
        if(newAge < 100) {
          this.age = newAge
        } else {
          throw new Exception("age not possible")
        }
      }
  }

  val vehicle: Person = new Person

  println(vehicle.getAge)

  vehicle.setAge(33333)







  /**
   *  default getters , setters
   *  fields, private this variable,
   *  auxillary constructors
   */


}