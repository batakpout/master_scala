package dsalgo.arrays

/**
 * i/p: 4 3 7 8 6 2 1
 * o/p: 3 7 4 8 2 6 1
 * a1 < a2 > a3 < a4 > a5
 * */
object DisplayZigZagArray_ImperativeStyle extends App {

  val arr = Array[Int](4, 3, 7, 8, 6, 2, 6)

  def swap(i1: Int, i2: Int) = {
    val temp = arr(i1)
    arr(i1) = arr(i2)
    arr(i2) = temp
  }

  def zig_zag(arr: Array[Int]) = {
    var i = 0;
    while (i < arr.length - 1) {
      if (i % 2 == 0) {
        if (arr(i) > arr(i + 1)) {
          swap(i, i + 1)
        }
      } else {
        if (arr(i) < arr(i + 1)) {
          swap(i, i + 1)
        }
      }
      i += 1
    }
  }

  zig_zag(arr)
  println(arr.toList)


}

object Functional_Style extends App {


  //TODO: don't know right now how to do it
  val l = List(4, 3, 7, 8, 6, 2, 6)

  def swap(p: (Int, Int)): (Int, Int) = {
    (p._2, p._1)
  }



  def zigZag(l: List[Int], skip: Boolean): List[Int] = ???


}
