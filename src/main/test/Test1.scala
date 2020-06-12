
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

class Test1 extends WordSpec with Matchers with MockitoSugar {

  "Hospital test" should {
    "return m1 as the first member" in {
      import teachingobaid.unittesting.Program1._
      val l = List("m1", "m2")
      val result = getHospitalFirstMemeber(l)

      result shouldBe "m1"
    }

    "return default no member when hospital is empty" in {
      import teachingobaid.unittesting.Program1._
      val l = Nil
      val result = getHospitalFirstMemeber(l)
      result shouldBe "no memeber"
    }
  }
}

