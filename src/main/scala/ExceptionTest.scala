

object LL extends App {

  trait NeraException extends Exception {
    def exception: Option[Throwable]
    def message: String

    override lazy val getMessage: String    = message
    override lazy val getCause  : Throwable = exception.get
  }

  case class GenericException(errorCode: Option[String] = None, message: String, exception: Option[Throwable] = None) extends NeraException
  println("came here1")
  val res = 10

   if(res == 100) {
     println("came here 2")
   } else {
     println("came here3")
     throw GenericException(message = "failed")
   }
  println("came here4")

}