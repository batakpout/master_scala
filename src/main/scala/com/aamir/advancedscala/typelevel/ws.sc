//def implicitly[T](implicit e: T): T = e
implicit val i: String = "hello"
implicitly[String]
// implicitly[Int] CTE

trait Permission[A] {
  def cal(a: A): A
}

/*
implicit val defaultPermission: Permission = Permission("0896")

val x = implicitly[Permission]
val x = implicitly[Permission].s*/




object AA {
  implicit val defaultPermission1 = new Permission[String] {
    override def cal(a: String) = "hello"
  }
}

object LL {
  def method[A: Permission](a: A) = implicitly[Permission[A]].cal(a)
}
import AA._

LL.method("abc")