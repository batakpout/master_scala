package devinsideyou

object ll extends App {
  lazy val b: Boolean = {
    println("in b")
    b
  }
  //println(b) Stackoverflow
}