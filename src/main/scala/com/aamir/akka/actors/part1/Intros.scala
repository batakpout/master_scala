package com.aamir.akka.actors.part1


import akka.actor.{Actor, ActorSystem, Props}

object Intros extends App {

  // part1 - actor systems
  val actorSystem = ActorSystem("firstActorSystem")
  println(actorSystem.name)

  /**
   * Actors are uniquely identified
   * Messages are asynchronous
   * Each actor may respond differently
   * Actor are really encapsulated
   */

  class WordCountActor extends Actor {
    // internal data
    var totalWords = 0

    // behavior
    //def receive: Receive = {
    def receive: PartialFunction[Any, Unit] = {
      case message: String =>
        println(s"[word counter] I have received: $message")
        totalWords += message.split(" ").length
      case msg => println(s"[word counter] I cannot understand ${msg.toString}")
    }
  }

  // part3 - instantiate our actor
  val wordCounter = actorSystem.actorOf(Props[WordCountActor], "wordCounter")
  val anotherWordCounter = actorSystem.actorOf(Props[WordCountActor], "anotherWordCounter")

  // part4 - communicate!
  wordCounter ! "I am learning Akka and it's pretty damn cool!" // "tell"
  anotherWordCounter ! "A different message"
  // asynchronous!

  object Person {
    def props(name: String) = Props(new Person(name))
    def apply(name: String) = Props(new Person(name))
  }

  class Person(name: String) extends Actor {
    override def receive: Receive = {
      case "hi" => println(s"Hi, my name is $name")
      case _ =>
    }
  }

  val person = actorSystem.actorOf(Person.props("Bob"))
  val person1 = actorSystem.actorOf(Props(new Person("twerp")))
  val person2 = actorSystem.actorOf(Person("sherp"))
  person ! "hi"

  /**
   * Message can be of any type
   * Messages must be immutable
   * Messages must be serializable
   * Actors have information about their context and about themselves
   * */
}
