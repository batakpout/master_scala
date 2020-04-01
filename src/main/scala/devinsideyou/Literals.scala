package devinsideyou

object Literals extends App {
  println("Line Feed (LF)")
  println("hello\nworld")
  println()

  println("Form Feed (FF)")
  println("hello\fworld")
  println()

  println("Horizontal tab (HT)")
  println("hello\tworld")
  println()

  println("BackSpace (BS)")
  println("hello\bworld")
  println("hello\bworld".size)
  println()

  println("Carriage Return (CR)")
  println("hello\rworld")
  println("hello\rworld".size) // just cursor removes it....
  println()
}

object Clock extends App {
   def now: String = {
     val formatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")
     java.time.LocalDateTime.now.format(formatter)
   }

  def inOneLine(in: String) = {
    val clearLine = "\u001b[2K"
    val carriageReturn = "\r"
    clearLine + carriageReturn + in
  }
    var i = 0
    while(i < 100) {
      Thread.sleep(1000)
      print(inOneLine(now))
      i += 1
    }
}

object LiteralExmp  extends App {
  'a' to 'z' foreach println
}

object Literalregex extends App {
  println("Hello world".replaceAllLiterally("l", "2"))
  println("Hello world".replaceAll("l(.*)", "2"))

  val favoriteNumber = 12226
  println("my favorite number is %s".format(favoriteNumber))

  val input = scala.io.StdIn.readLine()
  println(input)
}