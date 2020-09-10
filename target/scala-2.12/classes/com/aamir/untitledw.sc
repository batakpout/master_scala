val l = List(1, 2)
val m = List(3, 4)

l.map{x => m.map(y => x + y)}
l.flatMap{x => m.map(y => x + y)}

