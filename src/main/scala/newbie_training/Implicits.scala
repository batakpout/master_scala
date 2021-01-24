package newbie_training

/*
def x(implicit x : Int)
def x(implicit x : Int, y : Int)
def x (x : Int)(implicit y : Int)

 */

/**
 * Implicits
 * Implicit variables
 * Implicit parameters
 * Implicit methods
 * Implicit classes
 * etc etc
 */

object I1 extends App {

  val divide: PartialFunction[Int, Int] = {
    case x if x!=0 => 10 / x
  }
   val l = List(1,2,3,4,0,5).collect(divide)
  val x = List(42, "cat") collect { case i: Int => i + 1 }
  println(l)
  println(x)
}
