package newbie_training.assign.reddy

//1. Vector and List both inherit from Seq, and its been said Vectors are faster than List in many//
//operations e.g Update operation.//
//Can you write a program to prove this that Vector Update is way too faster than List//
//Updates.//

object question_1 extends App {
  import scala.concurrent.duration._
  import scala.util.Random
  import scala.language.postfixOps
  import collection.immutable.Seq
  private def quickPrint(text: String)(duration: Duration): Unit =
    println(s"[$text] - ${duration}")

  private def ranges(base: Int, n: Int): Seq[Range] = for {
    i <- 1 until n
  } yield 1 until math.pow(base, i).toInt
  private def measureAvgTime[T](n: Int)(op: => T): Duration = {
    def measureTime[T]: Long = {
      val startTime = System.nanoTime()
      op
      val endTime = System.nanoTime()
      (endTime - startTime)
    }
    val values: Seq[Long] = (1 until n).map(_ => measureTime)
    values.sum / n nanoseconds
  }

  val Iterations: Int = 8
  val Base: Int = 3
  val Repeats: Int = 8
  lazy val ranges: Seq[Range] = ranges(Base, Iterations)
  var idx = 0
  for {
    range <- ranges
  } {
    val list: List[Int] = range.toList
    val vector: Vector[Int] = range.toVector
    def quickPrintForList = quickPrint("List") _
    def quickPrintForVector = quickPrint("Vector") _
    def measureAvg = measureAvgTime(Repeats) _
    val random: Int = Random.nextInt(range.size)
    println(s"****** $Base^$idx elements *******")
    println()
    idx += 1
    println("**** APPLY ****")
    quickPrintForList(measureAvg(list(random)))
    quickPrintForVector(measureAvg(vector(random)))
    println()
    println("**** APPENDING ****")
    quickPrintForList(measureAvg(list :+ random))
    quickPrintForVector(measureAvg(vector :+ random))
    println()
    println("**** PREPENDING ****")
    quickPrintForList(measureAvg(random +: list))
    quickPrintForVector(measureAvg(random +: vector))
    println()
  }


}
