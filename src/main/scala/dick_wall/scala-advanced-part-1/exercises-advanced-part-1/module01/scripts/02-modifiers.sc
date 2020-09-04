val conversionKilosToPounds = 2.20462262185

class Person(val name: String, var weightInPounds: Double) {

  def weightInKilos: Double = weightInPounds / conversionKilosToPounds
  
  def weightInKilos_=(newWeight: Double): Unit = {
  	weightInPounds = newWeight * conversionKilosToPounds
  }

}

val fred = new Person("Fred", 120)

fred.weightInPounds

fred.weightInKilos

fred.weightInKilos = 60
fred.weightInKilos_=(60)

fred.weightInKilos

fred.weightInPounds

fred.weightInPounds = 125

fred.weightInPounds

fred.weightInKilos
