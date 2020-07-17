package teachingchotu.unittesting


case class User(name: String)

trait LoginService {
  def login(name: String, password: String): Option[User]
}

class RealLoginService extends LoginService {
  def login(name: String, password: String): Option[User] = {
    if (name == "obaid" && password == "wadefff") Some(User("obaid")) else None
  }
}