val l = List(Some(1), None, Some(3), None)
val optInt = Some(11)
optInt.flatMap {
  case n if n > 2 => Some(n - 1)
  case _ => Some(1)
}

optInt.flatMap { x => Some(x)
}
optInt.map { x => Some(x)
}
l.flatten