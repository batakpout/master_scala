val conversionKilosToPounds = 2.20462262185

case class Potato(weightInKilos: Double) {
  val weightInPounds = weightInKilos * conversionKilosToPounds
}

val p1 = Potato(0.75)

p1.weightInKilos

p1.weightInPounds


case class Person(name: String, weightInPounds: Double) {
  def weightInKilos = weightInPounds / conversionKilosToPounds
}

val p2 = Person("Fred", 120)

p2.weightInPounds

p2.weightInKilos
