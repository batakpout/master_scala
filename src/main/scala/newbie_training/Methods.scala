package newbie_training

object Methods1 extends App {

  //method parameters are vals by default

 def aMethod = "str"



  //tail recursion.


  //n =

  /*
     1.) f("Hi", 3)

     "Hi" + f("Hi", 2)
     "Hi" + f("Hi", 1)
     "Hi"
   */

   def factorial(n: Int): Int = {
     if( n <= 0 ) 1
     else n * factorial(n - 1)
   }

 // val fRes = factorial(100)
  //println(s"Factorial of 4 is : $fRes")

  // n = 3
  /*
     n! = n * (n - 1) * (n - 2) * (n - 3) ........ 1

     factorial(4)


   */
}