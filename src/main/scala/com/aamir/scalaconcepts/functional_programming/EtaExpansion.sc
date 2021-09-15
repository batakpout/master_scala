def calculatePropertyTax(monthlyIncome: Int, investments: List[String]):Int = 1000

val f = calculatePropertyTax _
val g: (Int, List[String]) => Int = calculatePropertyTax

class Tax {

  private[this]  val socialSecurityNumber:String = ""
  def calculatePropertyTax(monthlyIncome: Int, investments: List[String]):Int = 1000

}

def h(totalYearlyCost: List[Int])(f: (Int, List[String]) => Int): Int = 2000

h(Nil)(g)
h(Nil)(g)
h(Nil)(new Tax().calculatePropertyTax _)
