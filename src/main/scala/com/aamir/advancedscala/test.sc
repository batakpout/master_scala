import advancedscala.lazily.{Cons, MyStream}

/*val x:Double = 2.2345
val y = String.format("%.2f", x)

"%.2f".format(x)*/

def fibonacci(first: BigInt, second: BigInt): MyStream[BigInt] =
  first #:: fibonacci(second, first + second)

fibonacci(1, 2).take(10).foreach(println)