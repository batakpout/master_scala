package basicscala.basics

object DefaultandNamedargs1 extends App {
  /**
   * Default arguments should be last argument else u have to used named arguments while calling in parameter list
   */
    def tRcFac(num: Int) = {
        def fact(n: Int = 1, acc: Int): Int = {
          if(n == 1) acc
          else
            fact(n - 1, n * acc)
        }
      fact(acc = 1)
    }

  println(s"tRcFac of 5 is ${tRcFac(5)}")


  def savePicture1(format: String = "jpg", width: Int, height: Int): Unit = println("Saving picture")
  savePicture1(width = 12, height = 22) // named args

  def savePicture2(format: String, width: Int, height: Int = 112): Unit = println("Saving picture")
  savePicture2("bmp", 22)
}