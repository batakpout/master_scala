package koans

import org.scalatest.{FunSpec, Matchers, SeveredStackTraces}
import scala.language.reflectiveCalls

class Module04 extends FunSpec with Matchers with SeveredStackTraces {

  // using structural typing, define a method sameLength which takes 2 of any kind of object that defines length, returning
  // true if they are the same length, false otherwise. If you do it right, we should be able to compare a string
  // with a list and have true come out if they are the same length

  // uncomment the following to test your sameLength implementation
  /*
  describe("Function sameLength") {

    it("should compare 2 lists of the same length correctly") {
      sameLength(List(1, 2, 3), List("1", "2", "3")) should be(true)
    }
    it("should compare 2 lists of different length correctly") {
      sameLength(List(1, 2, 3), List("1", "2", "3", "4")) should be(false)
    }
    it("should compare a list and an array of the same length correctly") {
      sameLength(List(1, 2, 3), Array("1", "2", "3")) should be(true)
    }
    it("should compare a list and an array of different length correctly") {
      sameLength(List(1, 2, 3), Array("1", "2", "3", "4")) should be(false)
    }
    it("should compare a String and a List of the same length correctly") {
      sameLength("hello", List('H', 'E', 'L', 'L', 'O')) should be(true)
    }
    it("should compare a String and a List of the different length correctly") {
      sameLength("hello!", List('H', 'E', 'L', 'L', 'O')) should be(false)
    }

    // the following should not compile, please check that it doesn't
    it ("should not allow things that do not have a length to be compared") {
      assertDoesNotCompile("""sameLength(5, "Hello")""")
    }
  }
  */

  sealed trait Fruit

  case class Lemon(name: String, ph: Int) extends Fruit
  case class Grapefruit(name: String, ph: Int) extends Fruit
  case class Banana(name: String, potassium: Int) extends Fruit

  describe ("Using refinement types") {
    // create a method that returns the ph for a fruit but only if the fruit has a ph method

    // uncomment the following to test it
    /*
    it ("should give ph for a Lemon") {
      val lemon = Lemon("Jif", 4)
      phForFruit(lemon) should be (4)
    }

    it ("should also give ph for a Grapefruit") {
      val grapefruit = Grapefruit("Pink", 3)
      phForFruit(grapefruit) should be (3)
    }

    it ("should not compile for a banana") {
      val banana = Banana("Fife", 328)
      assertDoesNotCompile("phForFruit(banana)")
    }
    */

    // now create a mutable ListBuffer to which only Fruits with a ph can be added
    // and uncomment below to test it

    import scala.collection.mutable

    /*
    describe ("Using a collection of ph Fruits") {
      it ("should only allow Fruits with a ph to be added") {
        phFruits += Lemon("Jif", 4)
        phFruits += Grapefruit("Pink", 3)

        assertDoesNotCompile("""phFruits += Banana("Fife", 328)""")

        phFruits should be (List(Lemon("Jif", 4), Grapefruit("Pink", 3)))

        val meanPh = phFruits.map(_.ph.toDouble).sum / phFruits.length
        meanPh should be (3.5 +- 1e-6)
      }
    }
    */
  }

  // define an enumeration for DNA Nucleotides with the values A, C, G and T, and the names
  // Adenine, Cytosine, Guanine and Thymine so that the following tests pass, when uncommented

  /*
  describe("A string of DNA") {
    it("should map to a sequence of Nucleotide enumeration values") {
      import Nucleotide._
      val str = "GATTACA"
      val nseq = str.map { ch =>
        ch match {
          case 'A' => A
          case 'C' => C
          case 'T' => T
          case 'G' => G
        }
      }

      nseq should be(Seq(G, A, T, T, A, C, A))
    }

    it("should print out to the full names of the nucleotides") {
      import Nucleotide._
      val str = "GATTACA"
      val nseq = str.map { ch =>
        ch match {
          case 'A' => A
          case 'C' => C
          case 'T' => T
          case 'G' => G
        }
      }

      val fullNames = nseq.mkString(" ")
      fullNames should be("Guanine Adenine Thymine Thymine Adenine Cytosine Adenine")

    }
  }*/

}
