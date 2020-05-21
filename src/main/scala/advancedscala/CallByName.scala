package advancedscala

object CallByName1 extends App {

   def byValueFunction(x: Int) = 34
    byNameFunction(2+3)

   def byNameFunction(x: => Int) = 90
    byNameFunction(2+3)
}