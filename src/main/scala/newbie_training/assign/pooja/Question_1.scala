import scala.concurrent.duration._
import scala.util.Random
import scala.language.postfixOps
import scala.collection.immutable.Seq
object main extends App {
  private def prettyPrint(text: String)(duration: Duration): Unit =
    println(s"[$text] - ${duration}")

  private def ranges(base: Int, n: Int): Seq[Range] = for {
    i <- 1 until n
  } yield 1 until math.pow(base,i).toInt


  private def measureAvgExecTime[T](n: Int)(op: => T): Duration = {
    def measureExecTime[T]: Long = {
      val startTime = System.nanoTime()
      op
      val endTime = System.nanoTime()
      (endTime - startTime)
    }

    val values: Seq[Long] = (1 until n).map(_ => measureExecTime)
    values.sum / n nanoseconds
  }


  val Iterations: Int = 2
  val Base: Int = 2
  val Repeats: Int = 5

  lazy val ranges: Seq[Range] = ranges(3, 3)

  println(ranges)

  for {
    range <- ranges
  } {
    val list: List[Int] = range.toList
    val vector: Vector[Int] = range.toVector

    def prettyPrintForList = prettyPrint("List") _

    def prettyPrintForVector = prettyPrint("Vector") _

    def measureAvg = measureAvgExecTime(Repeats) _

    val random: Int = Random.nextInt(range.size)
    println("APPENDING ")
    prettyPrintForList(measureAvg(list :+ random))
    prettyPrintForVector(measureAvg(vector :+ random))
    println()

  }
}
