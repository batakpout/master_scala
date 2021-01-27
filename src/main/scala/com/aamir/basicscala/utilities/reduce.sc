List(1,3,4).reduceOption(_ + _)

trait Combine[A] {
  def combineWith(a: A): A
}

case class PotatoBag(weight: Double) extends Combine[PotatoBag] {
  override def combineWith(otherBag: PotatoBag): PotatoBag =
    PotatoBag(this.weight + otherBag.weight)
}

case class TruckOfPotatoes(potatoBags: PotatoBag*) {
  lazy val totalWeight =
    potatoBags
      .reduceOption((a, b) => a.combineWith(b))
      .map(_.weight)
      .getOrElse(1)
}

val pB1 = PotatoBag(9.2)
val pB2 = PotatoBag(4.2)
val pB3 = PotatoBag(2.2)

TruckOfPotatoes(pB1, pB2, pB3).totalWeight