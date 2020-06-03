//trait linearization
trait A  {
  def m1(): String
}
trait B extends A {
  override def m1(): String = "Hi B"
}

trait C extends A {
  override def m1(): String = "Hi c"
}


class D extends C with B {

}

new D().m1()

