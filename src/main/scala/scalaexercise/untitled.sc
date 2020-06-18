
val xs = List(1,2,3)
val ys = List(1,2,3)

xs.filter(_ % 2 == 0)
for (x <- xs if x % 2 == 0) yield x

xs.flatMap(x => ys.map(y => (x, y)))
for (x <- xs; y <- ys) yield (x, y)

xs.map(_ + 1)
for(x <- xs) yield x + 1

//so,
for {
  x <- xs if x % 2 == 0
  y <- ys
} yield (x, y)

xs.filter { x =>
  x % 2 == 0
}.flatMap { x =>
   ys.map { y =>
     (x, y)
   }
}