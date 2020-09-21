package newbie_training

object FunctionalProgramming1 extends App {

   //call by name
   def method1(g: Int => Int): Int = {
     g(100)
   }

  val f: Int => Int = (x: Int) => x + 2

   println {
     method1((x:Int) => x + 33)
   }




}