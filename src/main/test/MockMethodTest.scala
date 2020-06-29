import org.mockito.Mockito._
import org.scalatest.FunSpec
import org.scalatest.mockito.MockitoSugar
import teachingobaid.unittesting.{TestMockMethod, TestMockMethodImpl}


  class MySpec extends FunSpec with MockitoSugar  {

    val t = mock[TestMockMethodImpl]

    describe("my feature") {
      it("should work") {
        when(t.method1("")).thenReturn("mocked")
        when(t.method2("", 2)) thenCallRealMethod()
        assert(t.method2("", 2) == "mocked")
      }

      it("should work too") {
        println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$")
        when(t.method1("")).thenReturn("mocked")
        when(t.method2("", 2)) thenCallRealMethod()
        assert(t.method2("", 2) == "mocked")
      }
    }

  }