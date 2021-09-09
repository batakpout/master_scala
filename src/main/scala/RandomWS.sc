import Test.{groups, wordsSource}
import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, IOResult}
import akka.stream.alpakka.csv.scaladsl.{CsvParsing, CsvToMap}
import akka.stream.scaladsl.{FileIO, Merge, RunnableGraph, Sink, Source, SubFlow}
import akka.util.ByteString
import ttt.s

import java.io.File
import java.nio.file.{Path, Paths}
import scala.concurrent.Future

class CsvUtility(implicit val system: ActorSystem) {

  implicit val ex = system.dispatcher

  def processCsvFile(inputFile: File): Unit = {
    val inputFileName = inputFile.getName
    val csvFile: Path = Paths.get(inputFile.getPath)
    val source: Source[ByteString, Future[IOResult]] = FileIO.fromPath(csvFile)

    /*   val z = source.via(CsvParsing.lineScanner()).map(_.map(_.utf8String)).map(list => list.filterNot(x => x  == "sensor-id" || x =="humidity")).filter(_.nonEmpty).
         groupBy(10, x => x.head).fold("")((cate, word) =>  {

       }*/


    // z.runWith(Sink.foreach(println))

    /*    val wordsSource: Source[String, NotUsed] = Source(List("Akka", "is", "amazing", "learning", "substreams", "love", "like", ""))
        val groups: SubFlow[String, NotUsed, wordsSource.Repr, RunnableGraph[NotUsed]] = {
          wordsSource.groupBy(30, word => if (word.isEmpty) '\0' else word.toLowerCase().charAt(0))
        }*/

    //val sink = Sink.foreach[List[String]](println)
    //z.runWith(sink)
    /*    val sink: Sink[String, Future[Int]] = Sink.fold[Int, String](0)((count, word) => {
          println("Count===> " + count)
          val newCount = count + 1
          println(s"I just received $word, count is: $newCount")
          newCount
        })*/


    /*
        val xsss: Source[List[String], Future[IOResult]] = source
          .via(CsvParsing.lineScanner(','))
          .map(_.map(_.utf8String))*/

    //xsss.groupBy(100, list => list.groupBy())


    /*      .filter(row => row.getOrElse("dept", "") == "bowler")
          .runForeach(x => println(x))
          .map { _ =>
            println(s"Successfully processed the file $inputFileName")
            true
          }
          .recover {
            case _: Exception => println(s"Error in processing the file $inputFileName")
              false
          }*/

  }
}
object CsvTest extends App {
  implicit val system = ActorSystem("Csvtest")
  val file = new File("D:/abc.csv")
  new CsvUtility().processCsvFile(file)
}

object Test extends App {

  implicit val system = ActorSystem()
  implicit val materilaizer = ActorMaterializer()

  //1- grouping a stream by a certain function
  val wordsSource = Source(Map(1 -> "Akka", 1 -> "is", 2 -> "amazing", 2 -> "learning", 2 -> "substreams", 3 -> "love", 2 -> "like"))
  val groups: SubFlow[(Int, String), NotUsed, wordsSource.Repr, RunnableGraph[NotUsed]] = {
    wordsSource.groupBy(4, word => word._1)
  }


  val sink: Sink[(Int, String), Future[Done]] = Sink.foreach[(Int, String)] { x =>
    println(x._1)
    println(x._2)
  }

  val g: RunnableGraph[NotUsed] = groups.to(sink)
  g.run()
}

object Test2 extends App {

  implicit val system = ActorSystem()
  implicit val materilaizer = ActorMaterializer()
  val wordsSource: Source[String, NotUsed] = Source(List("Akka", "is", "amazing", "learning", "substreams", "love", "like", ""))
  val groups: SubFlow[String, NotUsed, wordsSource.Repr, RunnableGraph[NotUsed]] = {
    wordsSource.groupBy(30, word => if (word.isEmpty) '\u0000' else word.toLowerCase().charAt(0))
  }

  val sink: Sink[String, Future[Int]] = Sink.fold[Int, String](0)((count, word) => {
    println("Count===> " + count)
    val newCount = count + 1
    println(s"I just received $word, count is: $newCount")
    newCount
  })
  val g: RunnableGraph[NotUsed] = groups.to(sink)
  g.run()
}

object jj extends App {

  val l: List[List[String]] = List(List("s1", "80"), List("s1", "8880"), List("s2", "780"), List("s2", "380"), List("s2", "180"), List("s3", "833"))
  val r = l.groupBy(x => x.head).map { case (key, value) =>
    (key -> value.flatten.filterNot(_ == key))
  }
  println(r)
}

//  val sink: Sink[String, Future[Int]] = Sink.fold[Int, String](0)((count, word) => {
object ttt extends App {
  implicit val system = ActorSystem("Csvtest")

  def merge(m1:Map[String, List[String]], m2:Map[String, List[String]]): Map[String, List[String]] = {
    (m1.keySet ++ m2.keySet).map { i => i -> (m1.get(i).toList.distinct ::: m2.get(i).toList.distinct) }.toMap.map { case (k, v) =>
      (k -> v.flatten)
    }
  }
  val s = Source(List(List("s1", "80"), List("s1", "8880"), List("s2", "780"), List("s2", "380"), List("s2", "180"), List("s3", "833")))
  //val y: SubFlow[List[String], NotUsed, SubFlow[List[String], NotUsed, s.Repr, RunnableGraph[NotUsed]]#Repr, RunnableGraph[NotUsed]] = s.groupBy(maxSubstreams = 5, list => list.head).groupBy(5, x => x.head)
  val y: SubFlow[List[String], NotUsed, s.Repr, RunnableGraph[NotUsed]] = s.groupBy(maxSubstreams = 5, list => list.head)

  val c: Source[Map[String, List[String]], NotUsed] = y.fold(Map[String, List[String]]().empty){ (x, yy) =>
    val zz: List[String] = yy.zipWithIndex.filterNot(_._2 % 2 == 0).map(_._1)
    val m = Map(yy.head -> zz)
    merge(x, m)
  }.mergeSubstreams

  val d: Source[Map[String, List[String]], NotUsed] = y.fold(Map[String, List[String]]().empty) { (x, yy) =>
    val zz: List[String] = yy.zipWithIndex.filterNot(_._2 % 2 == 0).map(_._1)
    val m = Map(yy.head -> zz)
    merge(x, m)
  }.mergeSubstreams


  val ccc: Source[Map[String, List[String]], NotUsed] = Source.combine(c, d)(Merge(_))

  // ccc.to(Sink.foreach(println)).run()
  val combined = Source.combine(c, d)(Merge(_)).groupBy(5, xx => xx.head._1)

  val e = combined.fold(Map[String, List[String]]().empty){ (x, yy) =>
    merge(x, yy)
  }.mergeSubstreams

  e.runForeach(println)
}

object jeee extends App {

}