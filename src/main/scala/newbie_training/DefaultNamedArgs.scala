package newbie_training

object Program1 extends App {

  //Named Arguments
    def getName(firstName: String, lastName: String) = {
      println(s"firstName is: $firstName, lastName is: $lastName")
    }

   getName(lastName = "Mark", firstName = "waugh")


  /*
    We can specify names of method arguments when calling the method: Named Arguments
   */
}

object Program2 extends App {
  //Default Arguments

    def scanPhotos(pngFormat: String, jpgFormat: String = "defalut.jpg") = {
       println(pngFormat)
       println(jpgFormat)
    }

   val picName1 = "abc.png"
  val picName2 = "scen.jpg"

  //scanPhotos(picName1, picName2)

  scanPhotos(picName1)

}

object CallByNameValue extends App {

  // call by value
  def something() = {
    println("calling something")
    10
  }

  def callByValue(x: Int) = {
    println("x1=" + x)
    println("x2=" + x)
  }

  def callByName(x: => Int) = {
    println("x1=" + x)
    println("x2=" + x)
  }
  callByName(something())

  /*
     x argument in method1 gets computed as soon as u pass some value to it
     --> in call by value method computes the pass-in expression's value before go inside of the method
   */

}

object CallByNameValue1 extends App {

  //call by value
   def  printNanoTime(nanoTime: Long) = {
/*      println(s"Current Nano time: $nanoTime")
      //some complex computation of 30 line here
     println(s"Current Nano time: $nanoTime")

     println(s"Current Nano time: $nanoTime")

     println(s"Current Nano time: $nanoTime")

     println(s"Current Nano time: $nanoTime")*/

   }

  //call by name
  def  printNanoTimeByName(nanoTime: => Long) = {

    println(s"Current Nano time: $nanoTime")
    //some complex computation of 30 line here
    println(s"Current Nano time: $nanoTime")

    println(s"Current Nano time: $nanoTime")

    println(s"Current Nano time: $nanoTime")

    println(s"Current Nano time: $nanoTime")


  }

  printNanoTimeByName(System.nanoTime())

  // call by name
  /**
   *   Call by name method recomputes the passed-in expression's value every time its accessedd
   */
}