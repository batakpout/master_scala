var count = 0
def complexCompute(): Int = { count +=1; println("eval " + count); count }

val iter: Iterator[Int] = Iterator.continually[Int] { complexCompute() }
iter.takeWhile(_ < 3).foreach(println)


val sentences = List("hi", "how", "are", "you", "today", "madam")
Iterator.continually(List(1,2,3)).flatten.zip(sentences.iterator).foreach { x =>
  println(x._1 + ":" + x._2)
  println()
}