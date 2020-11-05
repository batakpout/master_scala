package loginobaid


import com.aamir.teachingdf.unittesting.{LoginJog, RealLoginService, User}
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.Futures
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.concurrent.Await

class LoginTest extends FunSuite with BeforeAndAfter with MockFactory {

  test("test login service") {

    val joginService = mock[LoginJog]
    (joginService.jog_in(_: String))
      .expects(
        "johndoe"
      )
      .returns(scala.concurrent.Future.failed(new Exception("errored future")))

    val service = new RealLoginService(joginService)
    val johndoe: scala.concurrent.Future[User] = service.login("johndoe", "secret")

    import scala.concurrent.duration._
    assert(Await.result(johndoe, 2.seconds) == User("johndoe"))

/*    (joginService.jog_in(_: String))
      .expects(
        "aamir"
      )
      .returns(scala.concurrent.Future.successful(User("aamir")))

    val johndoe2: scala.concurrent.Future[User] = service.login("aamir", "aamir")

    import scala.concurrent.duration._
    assert(Await.result(johndoe2, 2.seconds) == User("aamir"))*/

  }
}