/*

import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

class Test221 extends WordSpec with Matchers with MockitoSugar with org.scalatest.prop.Checkers {

  import org.scalatest.prop.TableDrivenPropertyChecks._
  forAll(
    Table(
      ("n", "d"),  // First tuple defines column names
      (  1,   2),  // Subsequent tuples define the data
      ( -1,   2),
      (  1,  -2),
      ( -1,  -2),
      (  3,   1),
      ( -3,   1),
      ( -3,   0),
      (  3,  -1),
      (  3,  Integer.MIN_VALUE),
      (Integer.MIN_VALUE, 3),
      ( -3,  -1)
    )
  ) { (n: Int, d: Int) =>

    whenever (d != 0 && d != Integer.MIN_VALUE
      && n != Integer.MIN_VALUE) {

      val f = new Fraction(n, d)

      if (n < 0 && d < 0 || n > 0 && d > 0)
        f.numer should be > 0
      else if (n != 0)
        f.numer should be < 0
      else
        f.numer should be === 0

      f.denom should be > 0
    }
  }
}

class Fraction(n: Int, d: Int) {

  require(d != 0)
  require(d != Integer.MIN_VALUE)
  require(n != Integer.MIN_VALUE)

  val numer = if (d < 0) -1 * n else n
  val denom = d.abs

  override def toString = numer + " / " + denom
}

*/
