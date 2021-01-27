
object StringSyntax {
  implicit class StringOps[A](a: A) {
    def vowels = true
  }
}

import StringSyntax._
val s = ""
s.vowels