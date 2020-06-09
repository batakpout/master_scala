val l = List(1,2,3,4,5,6)
val x = l.drop(1)
l zip x
res0 forall( x => x._1 < x._2)