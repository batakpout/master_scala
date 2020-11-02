def f1(x: Int)(f: Int => String)(implicit c: Boolean): String = {

  if(c) {
    f(x)
  } else {
    f(x) + " sla,_data"
  }
}
implicit val b: Boolean = false
val ll = f1(1) _
def func(y: String)(implicit x: Int):String = {
  x.toString + y
}


f1(10) { implicit x  =>

{
  (func("abc") + 3.toString)
}

}

val useNewAuth = false

def check(useNewAuth: Boolean, x: Any) = x match {
  case _: Int => "int"
  case _: String =>
    if(useNewAuth) "string"
    else "else string"
}

val x = check(true, "sss")

def x(x: Int)(implicit y: Double) = x + y

implicit val d:Double = 10.7
x(10)
