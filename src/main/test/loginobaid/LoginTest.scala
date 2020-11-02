package loginobaid


import com.aamir.teachingdf.unittesting.{LoginJog, LoginService, RealLoginService, User}
import org.mockito.Mockito.when
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._

class LoginTest extends FunSuite with BeforeAndAfter with MockFactory {

  test("test login service") {

    val joginService = mock[LoginJog]
    (joginService.jog_in( _: String))
      .expects(
        "johndoe"
      )
      .returns("johndoe")

    val service = new RealLoginService(joginService)
    val johndoe = service.login("johndoe", "secret")

    assert(johndoe == User("johndoe"))

  }

}