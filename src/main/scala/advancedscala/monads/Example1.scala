package advancedscala.monads

case class Bag[A](item: A) {
  def map[B](f: A => B): Bag[B] = Bag(f(item))

  def flatMap[B](f: A => Bag[B]): Bag[B] = f(item)
}

object Bag {
  def unit[A](v: A) = Bag(v)
}

sealed trait Nuts {
  val quantity: Int
  private val price: Double = 2.0

  def total = quantity * price
}

case class Peanut(quantity: Int) extends Nuts

case class GroundNut(quantity: Int) extends Nuts

case class CashewNut(quantity: Int) extends Nuts

case class MixedNuts(quantity: Int) extends Nuts

object Example1 extends App {
  val cashewBag: Bag[CashewNut] = Bag(CashewNut(40))
  val groundNutBag: Bag[GroundNut] = Bag(GroundNut(40))
  val peanutBag: Bag[Peanut] = Bag(Peanut(40))

  def applyDiscount(quantity: Int): Int =
    if (quantity > 20) (0.75 * quantity).toInt else quantity

  def printTotal(nut: Nuts): Unit =
    println(s"total is  ${result.item.total}")

  val f: Nuts => CashewNut = (x: Nuts) => CashewNut(applyDiscount(x.quantity))
  val test1: Bag[CashewNut] = cashewBag.map{ x => CashewNut(x.quantity + 10)}
  val result = for {
    a <- cashewBag.map(f)
    b <- groundNutBag.map(x => GroundNut(x.quantity + 20))
    c <- peanutBag.map(x => Peanut(x.quantity * 2))
  } yield {
    MixedNuts(a.quantity + b.quantity + c.quantity)
  }

  printTotal(result.item)

  /**
   * without monad
   * val newCashewBag = Bag(CashewNut(applyDiscount(cashewBag.item.quantity)))
   *
   * val newGroundBag = Bag(GroundNut(groundNutBag.item.quantity - 20))
   * val newPeanutBag = Bag(Peanut(peanutBag.item.quantity * 2))
   * val mixedNuts    = Bag(MixedNuts(newCashewBag.item.quantity +
   *                                    newGroundBag.item.quantity +
   *                                    newPeanutBag.item.quantity))
   *
   * printTotal(mixedNuts.item)
   */
}