import Test.{groups, wordsSource}
import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, IOResult}
import akka.stream.alpakka.csv.scaladsl.{CsvParsing, CsvToMap}
import akka.stream.scaladsl.{FileIO, Flow, Framing, Keep, Merge, RunnableGraph, Sink, Source, SubFlow}
import akka.util.ByteString
import ttt.s

import java.io.File
import java.nio.file.{Path, Paths}
import scala.collection.immutable
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

  def merge(m1: Map[String, List[String]], m2: Map[String, List[String]]): Map[String, List[String]] = {
    (m1.keySet ++ m2.keySet).map { i => i -> (m1.get(i).toList.distinct ::: m2.get(i).toList.distinct) }.toMap.map { case (k, v) =>
      (k -> v.flatten)
    }
  }

  val s = Source(List(List("s1", "80"), List("s1", "8880"), List("s2", "780"), List("s2", "380"), List("s2", "180"), List("s3", "833")))
  //val y: SubFlow[List[String], NotUsed, SubFlow[List[String], NotUsed, s.Repr, RunnableGraph[NotUsed]]#Repr, RunnableGraph[NotUsed]] = s.groupBy(maxSubstreams = 5, list => list.head).groupBy(5, x => x.head)
  val y: SubFlow[List[String], NotUsed, s.Repr, RunnableGraph[NotUsed]] = s.groupBy(maxSubstreams = 5, list => list.head)

  val c: Source[Map[String, List[String]], NotUsed] = y.fold(Map[String, List[String]]().empty) { (x, yy) =>
    val zz: List[String] = yy.zipWithIndex.filterNot(_._2 % 2 == 0).map(_._1)
    val m = Map(yy.head -> zz)
    merge(x, m)
  }.mergeSubstreams

  val d: Source[Map[String, List[String]], NotUsed] = y.fold(Map[String, List[String]]().empty) { (x, yy) =>
    val zz: List[String] = yy.zipWithIndex.filterNot(_._2 % 2 == 0).map(_._1)
    val m = Map(yy.head -> zz)
    merge(x, m)
  }.mergeSubstreams



  val l: Seq[Source[Map[String, List[String]], NotUsed]] = List(c, d)
  val x: Source[Map[String, List[String]], NotUsed] = l.fold(Source.empty){ (acc, s) =>
    Source.combine(s, acc)(Merge(_))
  }

  // ccc.to(Sink.foreach(println)).run()
  val combined = x.groupBy(5, xx => xx.head._1)

  val e: Source[Map[String, List[String]], NotUsed] = combined.fold(Map[String, List[String]]().empty) { (x, yy) =>
    merge(x, yy)
  }.mergeSubstreams

  e.runForeach(println)

}

object FileProcessing {

  def processFile(file: File) = {

  }
}

object FileProcessingTest extends App {

  implicit val system = ActorSystem()


  def merge(m1: Map[String, List[String]], m2: Map[String, List[String]]): Map[String, List[String]] = {
    (m1.keySet ++ m2.keySet).map { i => i -> (m1.get(i).toList.distinct ::: m2.get(i).toList.distinct) }.toMap.map { case (k, v) =>
      (k -> v.flatten)
    }
  }

  val filesList = List("D:/abc1.csv", "D:/abc2.csv").map(new File(_))

  val sourceSet: Seq[Source[Map[String, List[String]], Future[IOResult]]] = filesList map { file =>
    val csvFile: Path = Paths.get(file.getPath)

    val source: Source[List[String], Future[IOResult]] = FileIO.fromPath(csvFile).
      via(CsvParsing.lineScanner(',')).
      map(_.map(_.utf8String)).
      map(list => list.filterNot(x => x == "sensor-id" || x == "humidity")).filter(_.nonEmpty)

    val s: Source[Map[String, List[String]], Future[IOResult]] = source.groupBy(maxSubstreams = 5, list => list.head).
      fold(Map[String, List[String]]().empty) { (acc, grouped) =>
        merge(acc, Map(grouped.head -> grouped.zipWithIndex.filterNot(_._2 % 2 == 0).map(_._1)))
      }.mergeSubstreams
    s
  }
  val s: Source[Map[String, List[String]], Object] = sourceSet.fold(Source.empty){ (acc, finalSource) =>
    Source.combine(finalSource, acc)(Merge(_))
  }

  val combined = s.groupBy(5, xx => xx.head._1)

  val e: Source[Map[String, List[String]], Object] = combined.fold(Map[String, List[String]]().empty) { (x, yy) =>
    merge(x, yy)
  }.mergeSubstreams

  val totalLines: Source[Int, Object] = e.map { x =>
    x.map { case (_, v) =>
      v.size
    }.sum
  }.reduce(_ + _)

  val nanLines = e.map(x =>
    x.map { case (k, v) =>
      v.count(_ == "NaN")
    }.sum
  ).reduce(_ + _)

  val sink1 = Sink.foreach[Int](x => println("totalLines = " + x))
  val sink2 = Sink.foreach[Int](x => println("nanLines = " + x))
  totalLines.to(sink1).run()
  nanLines.to(sink2).run()

  val fR: Source[Map[String, (Int, Double, Int)], Object] = e.map { x =>
    x.map { case (k, v) =>
      val sortedList: List[String] = v.sorted
      val filterNonNanRecords = sortedList.filterNot(_ == "NaN").map(_.toInt)
      val avg = filterNonNanRecords.sum / sortedList.size
      (k -> (filterNonNanRecords.min, avg, filterNonNanRecords.max))
    }
  }

  fR.runForeach(println)


  //   val x1: Source[Map[String, List[String]], Future[IOResult]] = Source.future[Map[String, List[String]]](Future.successful(Map.empty))
  //   val x2: Source[Map[String, List[String]], Future[IOResult]] = Source.empty[Map[String, List[String]]]
  //  val r:   Source[Map[String, List[String]], NotUsed] = Source.combine(x1, x2)(Merge(_))
}

object rr extends App {
  val s: Source[String, Future[IOResult]] = FileIO.fromPath(Paths.get("a.csv"))
    .via(Framing.delimiter(ByteString(","), 256, true).map(_.utf8String))
}