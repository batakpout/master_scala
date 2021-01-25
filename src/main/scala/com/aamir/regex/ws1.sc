import scala.util.matching.Regex
import java.util.regex.{Matcher, Pattern}
import scala.util.Try

val testPhoneNumber: String = "111-222-222-3212"
val isPhoneNumber           = "[0-9]{3}-[0-9]{3}-[0-9]{4}".r.findAllMatchIn(testPhoneNumber)
isPhoneNumber.toList
//////
object FindEmail {

  private val regex  : Regex   = new Regex("([a-z]+)@([a-z]+)\\.([a-z]+)")
  private val pattern: Pattern = regex.pattern

  def apply(input: String): RegexFind = RegexFind(pattern.matcher(input))

  case class RegexFind(private val m: Matcher) {
    private lazy val find      : Boolean = m.find()
    private lazy val groupCount: Int     = m.groupCount()

    private lazy val matches: List[String] = (for {
      n <- 1 to groupCount
      group = Try(m.group(n))
      if group.isSuccess
    } yield group.get).toList

    override lazy val toString: String = s"match: $find, matches: $matches"
  }

}

val testEmail: String = "dropmeataamir@gmail.com"
val matches = FindEmail(testEmail)


////////

// with pattern matching
val testAddress: String = "123 elm st."
val isAddress = "([0-9]+) ([a-z]+) (st|blvd)\\.".r

testAddress match {
  case isAddress(number, streetName, streetType) =>
    println(s"streetName: $streetName $streetType , at: $number")
    assert(number.toInt == 123, number)
}