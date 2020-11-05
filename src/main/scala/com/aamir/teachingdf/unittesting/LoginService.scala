package com.aamir.teachingdf.unittesting


import scala.concurrent.ExecutionContext.Implicits.global

case class User(name: String)

trait LoginService {
  def login(name: String, password: String): scala.concurrent.Future[User]
}

class RealLoginService(loginJog: LoginJog) extends LoginService {
  def login(name: String, password: String): scala.concurrent.Future[User] = {
    val x = 10
    val y = x + 21
    println(y)
    loginJog.jog_in(name)recoverWith {
      case e =>
        scala.concurrent.Future.failed(e)
    }
  }
}


class LoginJog {
  def jog_in(x: String) = {
    scala.concurrent.Future(User(x))
  }
}