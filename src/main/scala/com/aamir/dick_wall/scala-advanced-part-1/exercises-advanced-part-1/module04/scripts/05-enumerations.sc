object Color extends Enumeration {
  val Red, Green, Yellow, Blue = Value
}

object Size extends Enumeration {
  val S = Value(1, "Small")
  val M = Value(2, "Medium")
  val L = Value(3, "Large")
  val XL = Value(4, "Extra Large")
}

Color.values

Size.values

Color.Green.id
Size.S.id

def shirt(color: Color.Value, size: Size.Value): String =
  s"A nice hawaiian shirt, color $color, size $size"

shirt(color = Color.Red, size = Size.XL)

// shirt(color = Size.S, size = Color.Yellow)