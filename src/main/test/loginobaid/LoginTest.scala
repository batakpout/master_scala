package loginobaid


import org.mockito.Mockito._
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.mockito.MockitoSugar
import teachingobaid.unittesting.{LoginService, User}

class LoginTest extends FunSuite with BeforeAndAfter with MockitoSugar {

  test("test login service") {

    val service: LoginService = mock[LoginService]

    when(service.login("johndoe", "secret")).thenReturn(Some(User("johndoe")))
    when(service.login("joehacker", "secret")).thenReturn(None)

    val johndoe = service.login("johndoe", "secret")
    val joehacker = service.login("joehacker", "secret")

    assert(johndoe.get == User("johndoe"))
    assert(joehacker.isEmpty)

  }

}