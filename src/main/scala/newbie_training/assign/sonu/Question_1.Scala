package newbie_training.assign.sonu

object Question_1 extends App {

  val start = System.currentTimeMillis()
  val v2 = "cat" :+ "bird" :+ "frog"
  println(v2)
  val changed = v2.updated(1, "fish")
  println(changed)
  val end = System.currentTimeMillis()
  println(s"Done in ${(end-start) / 1000} seconds")



  val start1 = System.currentTimeMillis()
  val myl = List(1,2,3,4,5)
  println (0 :: myl)
  val end1 = System.currentTimeMillis()
  println(s"Done in ${(end1-start1) / 1000} seconds")
}