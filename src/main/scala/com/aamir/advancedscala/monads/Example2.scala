//--> What has helped me is to think of a monad as a container with operations (map/flatMap/filter/foreach) that can act on the contents without extracting it first.

//For instance, this function “opens” the option to get its content:
object Example2 extends App {

  def display1(msg: String, prompt: Option[String] = None) = {
    prompt match {
      case None      => ()
      case Some(str) => print(str)
    }
    println(msg)
  }

  //Contrast this with:

  def display2(msg: String, prompt: Option[String] = None) = {
    prompt foreach print
    println(msg)
  }

  //where the option is told to print its content (if any).
}