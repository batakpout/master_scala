trait HeightAndWeight {
	var height: Double
	var weight: Double
}

trait HeightAndWeightAsGenerated {

	def height: Double
	def height_=(d: Double): Unit

	def weight: Double
	def weight_=(d: Double): Unit
}


class Person(val name: String) extends HeightAndWeight {
	private[this] var ht: Double = _
	private[this] var wt: Double = _

	def height: Double = ht
	def height_=(h: Double): Unit = {
		require(h > 0.0, "Height may not be zero or negative")
		ht = h
	}

	def weight: Double = wt
	def weight_=(w: Double): Unit = {
		require(w > 0.0, "Weight may not be zero or negative")
		wt = w
	}
}

val fred = new Person("Fred")

fred.height

fred.height = 172.0

fred.height

fred.weight

fred.weight = 65.0

fred.weight

fred.weight = -5.0


class TruckLoad extends HeightAndWeight {
	import scala.collection.mutable
	private[this] val propsMap = mutable.Map.empty[String, Double]

	def height: Double = propsMap.getOrElse("height", 0.0)
	def height_=(h: Double): Unit = propsMap("height") = h

	def weight: Double = propsMap.getOrElse("weight", 0.0)
	def weight_=(w: Double): Unit = propsMap("weight") = w

}

val truck = new TruckLoad

truck.height

truck.height = 20.0

truck.height
