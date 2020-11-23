
def someFunction1(x: Int) = x match {
  case 1 => Some("1")
  case 2 => None
  case 3 => Some("3")
}

def pf:PartialFunction[Int, Option[String]] = {
  case 1 => Some("1")
  case 2 => None
  case 3 => Some("3")
}

def someFunction(x: Int) = pf(x)
someFunction(2)
someFunction1(2)