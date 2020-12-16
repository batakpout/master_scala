def refTran(x: Int, y: Int) = x + y

val x = 10
val y = 20
val result = refTran(x, y)
val result2 = refTran(10, 20)
def effectFul(f1: Unit, f2: Unit): Unit= ()

val sE = println("Side-effect")
//effectFul(sE, sE)
effectFul(println("Side-effect"), println("Side-effect"))


