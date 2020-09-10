case class Person1(name: String) {
  case class LifePartner(name: String) {
    def describe: String = s"$name loves $name"
  }
}

val p1 = Person1("Fred")
val p1a = p1.LifePartner("Sally")
p1a.describe

case class Person2(name: String) { outer => // another name for the outer.this
  case class LifePartner(name: String) {
    def describe: String = s"${this.name} loves ${outer.name}"
  }
}

val p2 = Person2("Fred")
val p2a = p2.LifePartner("Sally")
p2a.describe


// self types with requirements:

import com.typesafe.scalalogging._
trait Loves { this: LazyLogging =>
  def loves(i1: AnyRef, i2: AnyRef) = logger.info(s"$i1 loves $i2")
}

case class Lovers(name1: String, name2: String) extends Loves with LazyLogging {
  def describe: Unit = loves(name1, name2)
}

Lovers("Kim", "Sally").describe

// See what happens if you try and take LazyLogging off of Lovers above...




