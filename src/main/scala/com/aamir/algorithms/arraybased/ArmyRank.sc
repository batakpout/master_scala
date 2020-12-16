def solution(ranks: Array[Int]) = { // write your code in Java SE 8

  val sRank = ranks.toSet

  def rec(sum: Int, rank: List[Int]): Int = rank match {
    case Nil => sum
    case h :: t => {
      if(sRank.contains(h + 1)) rec(sum + 1, t) else rec(sum, t)
    }
  }
  rec(0, ranks.toList)
}

solution(Array(3,4,3,0,2,2,3,0,0))