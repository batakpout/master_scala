package basicscala

object CBVvsCBN1 extends App {

    def calledByValue(x :Long) = { // x is actual value
       println(s"By value: $x")
       println(s"By value: $x")
    }

   def callByName(f: => Long) = { // here f = System.nanoTime()
     println(s"By Name: ${f}")
     println(s"By Name: ${f}")
   }

  calledByValue(System.nanoTime())
  callByName(System.nanoTime())

  println("-" * 20)

  def infinite():Int = 1 + infinite()

  def printFirst(x: Int, y: => Int) = println(x)

  printFirst(10, 20)
  //printFirst(infinite(), 10)
  printFirst(10, infinite())
}