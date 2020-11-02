package com.aamir.teachingdf.unittesting



case class User(name: String)

trait LoginService {
  def login(name: String, password: String): User
}

class RealLoginService(loginJog: LoginJog) extends LoginService {
  def login(name: String, password: String) = {
    val x = 10
    val y = x + 21
    println(y)
    User(loginJog.jog_in(name))
  }
}


class LoginJog {
  def jog_in(x: String) = {
    x
  }
}