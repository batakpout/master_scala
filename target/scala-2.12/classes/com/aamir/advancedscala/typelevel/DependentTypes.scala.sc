/**
 * In computer science and logic, a dependent type is a type that depends on a value.
 */

class Foo {
  class Bar
}
///
val foo1 = new Foo
val foo2 = new Foo

val a: Foo#Bar = new foo1.Bar
val b: Foo#Bar = new foo1.Bar
val c: Foo#Bar = new foo2.Bar

val c: foo1.Bar = new foo1.Bar
// val d: foo2.Bar = new foo1.Bar CTE

trait Fooo {
  trait Bar
  def bar: Bar
}

def fooo(f: Fooo): f.Bar = f.bar
/**
# means that we don’t refer to any specific instance, in this case Foo#Bar,
every Bar inside every instance of Foo will be a valid instance

. means that we can only refer the Bar instances that belong to a specific
instance of Foo
 **/