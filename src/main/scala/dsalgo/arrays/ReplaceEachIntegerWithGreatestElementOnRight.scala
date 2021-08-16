package dsalgo.arrays

/**
 * i/p: 10 8 2 4 3 7 1
 * o/p: 8 7 7 7 7 1 -1
 */
object ReplaceEachIntegerWithGreatestElementOnRight_Imperative extends App {


  val arr = Array(17, 18, 5, 4, 6, 1)

  def find(arr: Array[Int]) = {
    var max: Int = -1
    var i: Int = 0
    while (i < arr.length - 1) {
      var j = i + 1
      max = arr(j)
      while (j < arr.length) {
        if (arr(j) > max) {
          max = arr(j)
        }
        j = j + 1
      }
      arr(i) = max
      i += 1
    }
    arr(i) = -1
  }
  find(arr)
  arr foreach(x => print(x + ","))

}