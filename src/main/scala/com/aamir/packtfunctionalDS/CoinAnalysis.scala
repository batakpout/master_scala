package packtfunctionalDS

object MinCoinsRecursive extends App {

  val ways = 0

  //https://www.youtube.com/watch?v=V4J0gNtCPB8
  def minCoinChange(coins: List[Int], amount: Int): Int = amount match {
    case amount if amount == 0 => 0
    case amount if amount < 0  => Integer.MAX_VALUE - 2
    case _                     => {
      var result = Integer.MAX_VALUE
      coins foreach { coin =>
        println("amount: " + amount)
        println("coin: " + coin)
        val ways = 1 + minCoinChange(coins, amount - coin)
        if (result > ways) result = ways
        println("ways: " + ways)
        println("Result: " + result)
        println("========================")
      }
      result
    }
  }


  println(minCoinChange(List(1, 2, 5), 3))

}