package advancedscala.typelevel.typeclasses

import advancedscala.typelevel.typeclasses.Program3.Show

object Program3 extends App {

//type-class
trait Show[A] {
  def show(a: A): String
}

//type-instance and interface
object Show {

  //def show[A](a: A)(implicit sh: Show[A]) = sh.show(a)
  //def show[A: Show](a: A) = implicitly[Show[A]].show(a)

    def apply[A](implicit sh: Show[A]): Show[A] = sh

  //def show[A: Show](a: A) = Show.apply[A].show(a)
    def show[A: Show](a: A) = Show[A].show(a)

    implicit val intCanShow: Show[Int] = new Show[Int] {
      def show(int: Int): String = s"int $int"
    }

}

  implicit class ShowOps[A: Show](a: A) {
    def show = Show[A].show(a)
  }

  //import Show._
 // println(10.show)

}
object Program3Test1 extends App {
  import advancedscala.typelevel.typeclasses.Program3.Show._

  implicit class ShowOps[A: Show](a: A) {
    //def show = Show.apply[A].show(a)
    def show = Show[A].show(a)
  }
  println(10.show)
}
object Program3Test2 extends App {

  implicit class ShowOps[A](val a: A) extends AnyVal {
    def show(implicit sh: Show[A]) = sh.show(a)
  }

  println(20.show)

}