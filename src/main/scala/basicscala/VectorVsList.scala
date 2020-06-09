package basicscala

import scala.util.Random

object VectorVsList extends App {
  val maxRuns = 1000
  val maxCapacity = 1000000

  def getWriteTime(collection: Seq[Int]): Double = {
    val r = new Random
    val times = for {
      it <- 1 to maxRuns
    } yield {
      val currentTime = System.nanoTime()
      collection.updated(r.nextInt(maxCapacity), r.nextInt())
      System.nanoTime() - currentTime
    }

    times.sum * 1.0 / maxRuns
  }

  val numbersList = (1 to maxCapacity).toList
  val numbersVector = (1 to maxCapacity).toVector

  // keeps reference to tail
  // updating an element in the middle takes long
  println("total avg. time for 1k list updates = " + getWriteTime(numbersList))
  // depth of the tree is small
  // needs to replace an entire 32-element chunk
  println("total avg. time for 1k vector updates = " + getWriteTime(numbersVector))

}