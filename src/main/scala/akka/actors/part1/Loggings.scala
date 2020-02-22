package akka.actors.part1

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.event.Logging
object ActorLoggingDemo extends App {

  class SimpleActorWithExplicitLogger extends Actor {
    // 1. #explicit logging
    val logger = Logging.getLogger(context.system, this)
      override def receive: Receive = {
        /*
          1 - DEBUG
          2 - INFO
          3 - WARNING/WARN
          4 - ERROR
         */
        case message => logger.error(message.toString)// LOG it
    }
  }
  val system = ActorSystem("LoggingDemo")
  val actor = system.actorOf(Props[SimpleActorWithExplicitLogger])

  actor ! "Logging a simple message"
}

object ActorLoggingDemo2 extends App {

   //2. #actor logging
  class ActorLoggingExmp extends Actor with ActorLogging {
    override def receive: Receive = {
      case message:String => log.error(message.toString)
      case (a, b) => log.info("Two things, first {}, second {}", a, b)
    }
  }

  val system = ActorSystem("ActorLoggingExample")
  val actor = system.actorOf(Props[ActorLoggingExmp])

  actor ! "Logging a simple message, using trait"
  actor ! (22, 88)
}
