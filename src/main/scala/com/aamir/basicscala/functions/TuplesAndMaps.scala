package basicscala.functions

import scala.annotation.tailrec

object TuplesAndMaps extends App {

/*  // tuples = finite ordered "lists"
  val aTuple = (2, "hello, Scala")  // Tuple2[Int, String] = (Int, String)

  println(aTuple._1)  // 2
  println(aTuple.copy(_2 = "goodbye Java"))
  println(aTuple.swap)  // ("hello, Scala", 2)

  // Maps - keys -> values
  val aMap: Map[String, Int] = Map()

  val phonebook = Map(("Jim", 555), "Daniel" -> 789, ("JIM", 9000)).withDefaultValue(-1)
  // a -> b is sugar for (a, b)
  println(phonebook)
  println("phonebook default value ==>" + phonebook("Jim"))
  println("phonebook default value ==>" + phonebook("Kim"))
  println("phonebook default value ==>" + phonebook.get("Jim"))
  println("phonebook default value ==>" + phonebook.get("Kurutsuba"))

  // map ops
  println(phonebook.contains("Jim"))
  println(phonebook("Mary"))

  // add a pairing
  val newPairing: (String, Int) = "Mary" -> 678 // actually -> return Tuple2
  val newPhonebook = phonebook + newPairing
  println(newPhonebook)

  // functionals on maps
  // map, flatMap, filter
  println("****")
  println(phonebook.map(pair => pair._1.toLowerCase -> pair._2))

  println("filter keys...")
  // filterKeys
  println(phonebook.filterKeys(x => x.startsWith("J")))
  // mapValues
  println(phonebook.mapValues(number => "0245-" + number))

  // conversions to other collections
  println(phonebook.toList)
  println(List(("Daniel", 555)).toMap)
  val names = List("Bob", "James", "Angela", "Mary", "Daniel", "Jim")
  println(names.groupBy(name => name.charAt(0)))*/


  /*
    1.  What would happen if I had two original entries "Jim" -> 555 and "JIM" -> 900

        !!! careful with mapping keys.

    2.  Overly simplified social network based on maps
        Person = String
        - add a person to the network
        - remove
        - friend (mutual)
        - unfriend

        - number of friends of a person
        - person with most friends
        - how many people have NO friends
        - if there is a social connection between two people (direct or not)
   */

  def add(network: Map[String, Set[String]], person: String): Map[String, Set[String]] =
    network + (person -> Set())

  def friend(network: Map[String, Set[String]], a: String, b: String): Map[String, Set[String]] = {
    val friendsA: Set[String] = network.getOrElse(a, Set())
    val friendsB = network.getOrElse(b, Set())

    network + (a -> (friendsA + b)) + (b -> (friendsB + a))
  }

  def unfriend(network: Map[String, Set[String]], a: String, b: String): Map[String, Set[String]] = {
    val friendsA = network.getOrElse(a, Set())
    val friendsB = network.getOrElse(b, Set())

    network + (a -> (friendsA - b)) + (b -> (friendsB - a))
  }

  def remove(network: Map[String, Set[String]], person: String): Map[String, Set[String]] = {
    def removeAux(friends: Set[String], networkAcc: Map[String, Set[String]]): Map[String, Set[String]] =
      if (friends.isEmpty) networkAcc
      else removeAux(friends.tail, unfriend(networkAcc, person, friends.head))

    val unfriended = removeAux(network(person), network)
    unfriended - person
  }

  def nFriends(network: Map[String, Set[String]], person: String): Int =
    if (!network.contains(person)) 0
    else network(person).size

  def mostFriends(network: Map[String, Set[String]]): (String, Set[String]) =
    network.maxBy[Int](pair => pair._2.size)

  def nPeopleWithNoFriends(network: Map[String, Set[String]]): Int =
    network.count(_._2.isEmpty)

  def socialConnection(network: Map[String, Set[String]], a: String, b: String): Boolean = {
    @tailrec
    def bfs(target: String, consideredPeople: Set[String], discoveredPeople: Set[String]): Boolean = {
      if (discoveredPeople.isEmpty) false
      else {
        val person = discoveredPeople.head
        if (person == target) true
        else if (consideredPeople.contains(person)) bfs(target, consideredPeople, discoveredPeople.tail)
        else bfs(target, consideredPeople + person, discoveredPeople.tail ++ network(person))
      }
    }

    bfs(b, Set(), network(a) + a)
  }

  val empty: Map[String, Set[String]] = Map()

  val network1 = add(empty, "Kunal")
  val network2 = add(network1, "Pooja")
  val network3 = add(network2, "Reddy")

  val network4 = friend(network3, "Kunal", "Reddy")

  val network5 = add(network4, "Shristi")
  val network6 = add(network5, "Deena")

  val network7 = friend(network6, "Kunal", "Deena")

  val network8 = unfriend(network7, "Kunal", "Reddy")

  val network9 = friend(network8, "Pooja", "Shristi")

  val network10 = remove(network9, "Reddy")

  val net11 = friend(network10, "Kunal", "Pooja")

  println(net11)
  println(socialConnection(net11, "Kunal", "Reddy"))

 // count of persons with no friends
/*
  val network: Map[String, Set[String]] = add(add(add(add(add(empty, "Pooja"),
    "Reddy"), "Kunal"), "Sonu"), "Shristi")
  println(network)

  val newSocialNetwork1 = friend(network, "Pooja", "Deena")
  val newSocialNetwork2 = friend(newSocialNetwork1, "Pooja", "Shristi")
  val newSocialNetwork3 = friend(newSocialNetwork2, "Sonu", "Pooja")
  val newSocialNetwork4 = friend(newSocialNetwork3, "Reddy", "Pooja")

  println(newSocialNetwork4)
  println(s"Poojas friends total: ${nFriends(newSocialNetwork4, "Pooja")}")
  println(s"Most friends: ${mostFriends(newSocialNetwork4)}")
  println(s"People count with no friends : ${nPeopleWithNoFriends(newSocialNetwork4)}")
  println("------")
  val res = socialConnection(newSocialNetwork4, "Pooja", "Deena")
  println(res)
  println("------")
  val newSocialNetwork5 = unfriend(newSocialNetwork4, "Pooja", "Deena")
  println(newSocialNetwork5)

  val newSocialNetwork6 =  remove(newSocialNetwork5, "Pooja")
  println(newSocialNetwork6)
*/

  // Jim,Bob,Mary
  //val people = add(add(add(empty, "Bob"), "Mary"), "Jim")
  //val jimBob = friend(people, "Bob", "Jim")
  //val testNet = friend(jimBob, "Bob", "Mary")

  //println(testNet)



  //println(nFriends(testNet, "Bob"))

 // println("most friends..")
  //println(mostFriends(testNet))

  //println(nPeopleWithNoFriends(testNet))



  //println(socialConnection(testNet, "Mary", "Jim"))
  //println(socialConnection(network, "Mary", "Bob"))


}

object maby extends App {

  case class G(name:String, price: Int)
  val g = List(G("a", 10), G("c", 22), G("d", 1))
  println(g.maxBy(_.price))

  def duplicate[T](n: Int, l: List[T]): List[T] = ???
}