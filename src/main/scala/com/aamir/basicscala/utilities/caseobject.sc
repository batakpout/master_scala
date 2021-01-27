object A {
  lazy val a: String = "a"
}

case object Foo {}

val a: A.type = A
val foo: Foo.type = Foo

assert(a == A)
assert(foo == Foo)

val b: A.type  = A // same as a, single instance

println(s"'a' is ${a.toString}")
println(s"'foo' is ${foo}")

assert(a.a == "a")
assert(foo.toString == "Foo")