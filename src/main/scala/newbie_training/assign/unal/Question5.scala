object Question5 extends App{

  val s = Seq(1,1,1,1,1,2,2,2,2,2,2,2,3,3,3,3,3,4,4,4,4,4,4,4,4)
  println(s.groupBy(l => l).map(t => (t._1, t._2.length)))

}
