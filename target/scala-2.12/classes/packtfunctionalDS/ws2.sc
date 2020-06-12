import scala.collection.immutable.TreeSet

val l = List(1,2,3,4,5,6)
val x = l.drop(1)
l zip x
res0 forall( x => x._1 < x._2)

val words = List("one", "two", "one", "three")
words.groupBy(identity).map(x => (x._1, x._2.length))


List(1,2,3,4,5,6).sliding(2,1).toList

val s = Set(1,2,3,4)

val ss: Boolean = s(22) //no order

//for order
val t = TreeSet(1,2,3,4,5)
//implemented as RedBlack tree, i.e a balanced binary search tree. O(log n) lookup.
t(0)







