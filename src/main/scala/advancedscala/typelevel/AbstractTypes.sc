/**
 * An Abstract Type is a type that is not known yet and we can define later,
 * it is defined with the keyword type,
 * it basically works for types like the def keyword works for values:
 */

trait Foo {
  type T
  def foo: T
}

object FooString extends Foo {
  type T = String
  def foo: T = "Foo-String"
}

object FooInt extends Foo {
  type T = Int
  def foo: T = 10
}

def getValue(f: Foo): f.T = f.foo

getValue(FooString)
getValue(FooInt)