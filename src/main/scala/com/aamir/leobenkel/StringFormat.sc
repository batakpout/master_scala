val i = 5
val outputLeadingZeros = f"$i%04d"
assert(outputLeadingZeros == "0005")

// customize leading zeros
val length: Int = 4
// try with 0, 1, 2, ' '
val c: Char = ' '
println(s"%$c${length}d".format(i))

// truncated decimals and leading zeros
val infiniteDouble: Double = 10 / 3.0
println(f"$infiniteDouble%08.4f")

// dynamic truncated decimals and leading zeros
val totalCharacterNumber: Int = 7
val decimalQuantity: Int = 3
assert(totalCharacterNumber > decimalQuantity)

val outputTruncDecimalAndLeadZero = s"%0$totalCharacterNumber.${decimalQuantity}f".
                                          format(infiniteDouble)
assert(outputTruncDecimalAndLeadZero == "003.333")

// using locale to format things
import java.util.Locale
import java.text.NumberFormat

val bigNumber: Long = 123345567
val formatNumberFR = NumberFormat.getIntegerInstance(Locale.FRANCE)
println(formatNumberFR.format(bigNumber))
val formatNumberUS = NumberFormat.getIntegerInstance(Locale.US)
println(formatNumberUS.format(bigNumber))