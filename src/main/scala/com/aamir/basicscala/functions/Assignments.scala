object SocialNetwork extends App {

  type newSocialNetwork = Map[String, Set[String]]

  def add(network: Map[String, Set[String]], person: String): newSocialNetwork = ???
  def friend(network: Map[String, Set[String]], a: String, b: String): newSocialNetwork = ???
  def unfriend(network: Map[String, Set[String]], a: String, b: String): newSocialNetwork = ???

  // using tail recursion, as u have to unfriend this person from other's social network
  def remove(network: Map[String, Set[String]], person: String):newSocialNetwork = ???
  def nFriends(network: Map[String, Set[String]], person: String): Int = ???
  def mostFriends(network: Map[String, Set[String]]): (String, Set[String]) = ???
  def nPeopleWithNoFriends(network: Map[String, Set[String]]): Int = ???
  def socialConnection(network: Map[String, Set[String]], a: String, b: String): Boolean = ???

}