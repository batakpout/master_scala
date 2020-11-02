package newbie_training.assign.unal

object question1 extends App{
  var v = Vector[Int]()
  for (i <- 1 to 50000) v = v:+ i
  val t1 = System.nanoTime
  val duration = (System.nanoTime - t1) / 1e9d
  println(duration)
  var l = List[Int]()
  for (i <- 1 to 50000) l = l:+ i
  val t2 = System.nanoTime
  val duration1 = (System.nanoTime - t2) / 1e9d
  println(duration1)
}
