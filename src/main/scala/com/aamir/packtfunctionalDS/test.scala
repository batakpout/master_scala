package packtfunctionalDS

import scala.collection.JavaConversions._

object coin {

  // m is size of coins array (number of different coins)
  def minCoins(coins: Array[Int], m: Int, V: Int): Int = {
    // base case
    if (V == 0) 0
    // Initialize result
    var res: Int = java.lang.Integer.MAX_VALUE
    for (i <- 0 until m if coins(i) <= V) {
      val sub_res: Int = minCoins(coins, m, V - coins(i))
      // result can minimized
      if (sub_res != java.lang.Integer.MAX_VALUE && sub_res + 1 < res)
        res = sub_res + 1
    }
    // Check for INT_MAX to avoid overflow and see if
    // Check for INT_MAX to avoid overflow and see if
    res
  }

  def main(args: Array[String]): Unit = {
    val coins: Array[Int] = Array(9, 6, 5, 1)
    val m: Int = coins.length
    val V: Int = 11
    println("Minimum coins required is " + minCoins(coins, m, V))
  }

}
