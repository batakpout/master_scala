import scala.io.Source
import scala.util.Try

object SingleFileTest extends App {

  def readAsString(fileName: String): Try[String] = {
    Try {
      val source = Source.fromFile(fileName)
      val fileContents = source.getLines().mkString
      source.close()
      fileContents
    }
  }

  val res = readAsString("D:/testaaa")
  println(res.isSuccess)
  //println(res.get)

}