package basicscala.functions

object PartialFunction1 extends App {
/*  val xxx = {
    case x: Int => x.toString
    case _      => ""
  }
  val yyy = xxx.lifted*/

  val pf: PartialFunction[Int, Boolean] = { case i if i > 0 => i % 2 == 0}
  val liftedPF: Int => Option[Boolean] = pf.lift

  def l[A]:PartialFunction[Int, Int] = List[Int](1, 2, 3)
  val lifted: Int => Option[Int] = l.lift
  val x: Option[Int] = lifted(0)

  /**
   * val l:PartialFunction[Int, A] = List ...Int index, A type of value
   * val m:Int => Option[A] = l.lifted
   */


}