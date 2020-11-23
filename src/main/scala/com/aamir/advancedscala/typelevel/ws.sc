//def implicitly[T](implicit e: T): T = e
implicit val i: String = "hello"
implicitly[String]
// implicitly[Int] CTE

case class Permission(s: String)

implicit val defaultPermission: Permission = Permission("0896")

val x = implicitly[Permission]
val x = implicitly[Permission].s