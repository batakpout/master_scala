import FileProcessingTest.{filesList, s}
import akka.actor.ActorSystem
import akka.stream.alpakka.csv.scaladsl.CsvParsing
import akka.stream.scaladsl.{FileIO, Merge, Sink, Source}

import java.io.File
import java.nio.file.{Path, Paths}

object CsvProcessor {

  implicit val system = ActorSystem("FileProcessor")
  implicit val d = system.dispatcher

  def merge(m1: Map[String, List[String]], m2: Map[String, List[String]]): Map[String, List[String]] = {
    (m1.keySet ++ m2.keySet).map { i => i -> (m1.get(i).toList.distinct ::: m2.get(i).toList.distinct) }.toMap.map { case (k, v) =>
      (k -> v.flatten)
    }
  }

  def logProcessedMeasurements(source: Source[Map[String, List[String]], Object]) = {
    val processedSource = source.map { x =>
      x.map { case (_, v) =>
        v.size
      }.sum
    }.reduce(_ + _)
    processedSource.to(Sink.foreach(x => println(s"Num of processed measurements: $x"))).run()
  }

  def logFailedMeasurements(source: Source[Map[String, List[String]], Object]) = {
    val failedSource = source.map(x =>
      x.map { case (k, v) =>
        v.count(_ == "NaN")
      }.sum
    ).reduce(_ + _)
    failedSource.to(Sink.foreach(x => println(s"Num of failed measurements: $x"))).run()

  }

  def calculateMinAvgMax(source: Source[Map[String, List[String]], Object]) = {
    source.map { x =>
      x.map { case (k, v) =>
        val sortedList: List[String] = v.sorted
        if (sortedList.forall(_ == "NaN")) {
          (k -> ("NaN", "NaN", "NaN"))
        } else {
          val filterNonNanRecords = sortedList.filterNot(_ == "NaN").map(_.toInt)
          val avg = filterNonNanRecords.sum / filterNonNanRecords.size
          (k -> (filterNonNanRecords.min.toString, avg.toString, filterNonNanRecords.max.toString))
        }
      }
    }
  }

  def sortList(list: List[(String, (String, Int, String))]) = {
    scala.util.Sorting.stableSort(list,
      (e1: (String, (String, Int, String)), e2: (String, (String, Int, String))) => e1._2._2 > e2._2._2).toList
  }

  def mapToListTuple(items: Seq[Map[String, (String, String, String)]]) = {
    items.flatMap { x =>
      x.toList.map { x =>
        (x._1, (x._2._1, x._2._2, x._2._3))
      }
    }.toList
  }

  def convertAvgStringToInt(list: List[(String, (String, String, String))]) = {
    list.map { x =>
      (x._1, (x._2._1, x._2._2.toInt, x._2._3))
    }
  }

  def convertAvgIntToString(list: List[(String, (String, Int, String))]) = {
    list.map { x =>
      (x._1, (x._2._1, x._2._2.toString, x._2._3))
    }
  }

  def processFiles(filesList: List[File]) = {

    val sourceSet = filesList map { file =>
      val csvFile: Path = Paths.get(file.getPath)

      val source = FileIO.fromPath(csvFile).
        via(CsvParsing.lineScanner(',')).
        map(_.map(_.utf8String)).
        map(list => list.filterNot(x => x == "sensor-id" || x == "humidity")).filter(_.nonEmpty)

      source.groupBy(maxSubstreams = 5, list => list.head).
        fold(Map[String, List[String]]().empty) { (acc, grouped) =>
          merge(acc, Map(grouped.head -> grouped.zipWithIndex.filterNot(_._2 % 2 == 0).map(_._1)))
        }.mergeSubstreams

    }
    val combinedSources = sourceSet.fold(Source.empty) { (acc, finalSource) =>
      Source.combine(finalSource, acc)(Merge(_))
    }
    val mergedSources = combinedSources.groupBy(5, x => x.head._1).
      fold(Map[String, List[String]]().empty) { (i, j) =>
        merge(i, j)
      }.mergeSubstreams

    logProcessedMeasurements(mergedSources)
    logFailedMeasurements(mergedSources)
    val minAvgMaxedRecords = calculateMinAvgMax(mergedSources)
    for {
      items <- minAvgMaxedRecords.runWith(Sink.seq)
      recordsAsList = mapToListTuple(items)
      partitionedList = recordsAsList.partition(_._2._1 == "NaN")
      avgIntList = convertAvgStringToInt(partitionedList._2)
      avgStringList = convertAvgIntToString(sortList(avgIntList))
      _ = println("Sensors with highest avg humidity:")
      _ = println("sensor-id, min, avg, max")
      _ = (avgStringList ::: partitionedList._1).foreach(sensorRecord =>
        println(sensorRecord._1, sensorRecord._2._1, sensorRecord._2._2, sensorRecord._2._3)
      )


    } yield ()


  }
}

object Main extends App {

  def getListOfFiles(dir: String): List[String] = {
    val file = new File(dir)
    file.listFiles.filter(_.isFile)
      .filter(_.getName.endsWith(".csv"))
      .map(_.getPath).toList
  }

  val fileList = getListOfFiles(args(0))
  println(s"Num of processed files: ${fileList.size}")
  CsvProcessor.processFiles(fileList.map(new File(_)))
}