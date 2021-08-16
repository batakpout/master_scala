package dsalgo.arrays

/**
 * for the array [2 5 9 3 1 12 6 8 7]
 * Next greater for 2 is 5
 * Next greater for 5 is 9
 * Next greater for 9 is 12
 * Next greater for 3 is 12
 * Next greater for 1 is 12
 * Next greater for 12 is -1
 * Next greater for 6 is 8
 * Next greater for 8 is -1
 * Next greater for 7 is -1
 */

//imperative style using arrays with n^2 complexity.
object NextGreatestElementToRight_Imperative_N_Square_Complexity extends App {

  val arr = Array(2, 5, 9, 3, 1, 12, 6, 8, 7)
  var cond: Boolean = _
  var next: Int = _

  for (i <- arr.indices) {
    var j = i + 1
    cond = true
    next = -1
    while (j < arr.length && cond) {
      if (arr(i) < arr(j)) {
        next = arr(j)
        cond = false
      }
      if (cond) j = j + 1
    }
    println(arr(i) + "-------> " + next)
  }

}

//iterative using Stack with n complexity
object NextGreatestElementToRight_Imperative_Using_Stack_N_Complexity extends App {

}


//functional style
object NextGreatestElementToRight_Functional_Style_N_Complexity extends App {


  def outer(list: List[Int], acc: List[(Int, Int)]): List[(Int, Int)] = list match {
    case Nil       => acc
    case h :: tail => {
      def inner(elem: Int, tail: List[Int]): (Int, Int) = tail match {
        case Nil                   => (elem, -1)
        case h :: _ if elem < h => (elem, h)
        case _ :: rest             => inner(elem, rest)
      }

      outer(tail, inner(h, tail) :: acc)
      //outer(tail, acc :+ inner(h, tail))
    }
  }

  val list = List(2, 5, 9, 3, 1, 12, 6, 8, 7)

 println{
   outer(list, Nil)
 }

}
