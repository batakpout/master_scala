package packtfunctionalDS

object TraitWarnings extends App {
  trait C
  //sealed trait C
  case class B() extends C
  case class D() extends C

  def m(a: C) = a match { //when using sealed trait, then compiler warning, match may not be exhaustive.
    case B() => println("-- type B----")
  }
  //m(D())
}