package com.aamir.basicscala.enums

/**
 * warning: match may not be exhaustive.Since we are using sealed,
  the compiler can know exactly the number of options available for this type. So the compiler can help us !
 */
object Program1 extends App {

  object Ores {

    sealed abstract class Ore(val pricePerKg: Double)

    case object Iron extends Ore(5)
    case object Gold extends Ore(5)
    case object Zinc extends Ore(5)
    case object Nickel extends Ore(1)

  }

  import Ores._

  val o: Ore = Zinc

  val result = o match {
    case Iron   => 1
    case Gold   => 2
    case Zinc   => 3
    case Nickel => 4
  }
  assert(result == 3, result)


  case class Box(ore: Ore, mass: Double) {
    lazy val value: Double = ore.pricePerKg * mass

    override def toString = s"[$ore x ${mass}Kg]"
  }

  val boxes = List(
    Box(Nickel, 5),
    Box(Iron, 10),
    Box(Zinc, 5),
    Box(Gold, 5)
  )

  val totalWeight = boxes.map(_.mass).sum
  val totalValue = boxes.map(_.value).sum

  val totalWeightGold = boxes.filter(_.ore == Gold).map(_.mass).sum
  val totalWeightNickel = boxes.filter(_.ore == Nickel).map(_.mass).sum

  println(s"Shipment: $boxes")
  println(s"Total weight: ${totalWeight}Kg | Total Value: $$${totalValue}")
 // val r: Map[Ore, List[Box]] = boxes.groupBy(_.ore)
  boxes.groupBy(_.ore).foreach {
    case (Iron, _) => println(s"- a lot of iron")
    case (ore, boxes)  =>
      val totalWeight = boxes.map(_.mass).sum
      val totalValue  = boxes.map(_.value).sum
      println(
        s"- $ore: Total weight: ${totalWeight}Kg | Total Value: ${totalValue}"
      )
  }
  println(
    "Congratulations ! 'Many of us never realize our greatness because we become sidetracked by secondary activities' - Less Brown"
  )
}