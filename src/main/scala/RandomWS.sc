import scala.collection.mutable

var myMap = mutable.Map[Int, List[String]]().empty

(1 to 3) foreach { x =>
  myMap.put(x, List(s"valueA-${x}", s"valueB-${x}"))
}
myMap
myMap.put(1, List("kkbg"))
myMap
