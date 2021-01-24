package com.aamir.akka.actors


import akka.actor.{Actor, ActorSystem, Props}

object EventSourcing extends App {

  case class Book(title: String, authors: List[String])

  class BookPublisher extends Actor {
    def receive = {
      case book: Book =>
        println(s"Yeah! Publishing a new Book: $book")
        context.system.eventStream.publish(book)
    }
  }

  class BookSubscriber extends Actor {
    override def preStart() :Unit = context.system.eventStream.subscribe(self, classOf[Book])
    def receive: Receive = {
      case book:Book => println(s"My name is ${self.path.name} b.a I have received a book: $book")
    }
  }

   val system = ActorSystem("publisher-subscriber-example")

  val bookPublisher = system.actorOf(Props[BookPublisher], name="book-publisher")

  val subscriber1 = system.actorOf(Props[BookSubscriber],name="subscriber-1")
  val subscriber2 = system.actorOf(Props[BookSubscriber],name="subscriber-2")

  //Now, going to publish b.a book.
  bookPublisher ! Book(title = "A book title", authors = List("Author", "Another author"))

  println(s">>>>${Thread.sleep(5000)}")

  system.eventStream.unsubscribe(subscriber2, classOf[Book])


  bookPublisher ! Book(title = "Another book title", authors = List("Another author"))

}