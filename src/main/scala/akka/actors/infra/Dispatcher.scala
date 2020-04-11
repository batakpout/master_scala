package akka.actors.infra

/**
 * Dispatcher are in-charge of delivering and handling messages within an actorsystem.
 *
 * Dispatchers are akka's engine...they make akka tick by
 * 1. implementing scala.concurrent.ExecutionContext
 * 2. registering an actor's mailbox for execution
 *
 * Dispatcher determine execution time and context and therefore provide the physical capabilities for scalaing up
 *
 */

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.actors.infra.Router3.Slave
import akka.routing.FromConfig
import com.typesafe.config.ConfigFactory

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

object Dispatcher1 extends App {

  class Counter extends Actor with ActorLogging {
    var count = 0

    override def receive: Receive = {
      case message =>
        count += 1
        log.info(s"[$count] $message")
    }
  }

  val system = ActorSystem("DispatchersDemo")
  val actors = for (i <- 1 to 10) yield {
    system.actorOf(Props[Counter].withDispatcher("my-dispatcher"), s"counter_$i")
  }

  val r = new Random()

  for (i <- 1 to 1000) {
    actors(r.nextInt(10)) ! i
  }

}

object Dispatcher2 extends App {


  class Counter extends Actor with ActorLogging {
    var count = 0

    override def receive: Receive = {
      case message =>
        count += 1
        log.info(s"[$count] $message")
    }
  }


  // method #2 - from config
  val system = ActorSystem("DispatcherConfigDemo")
  val actorRef = system.actorOf(Props[Counter], "rtjvm")

  actorRef ! "hi"

}

object Dispatcher3 extends App {
  /**
   * Dispatchers implement the ExecutionContext trait
   */
  class Counter extends Actor with ActorLogging {
    var count = 0

    override def receive: Receive = {
      case message =>
        count += 1
        log.info(s"[$count] $message")
    }
  }

  class DBActor extends Actor with ActorLogging {
    // solution #1
    //implicit val executionContext: ExecutionContext = context.system.dispatchers.lookup("my-dispatcher")
    implicit val executionContext: ExecutionContext = context.dispatcher
    // solution #2 - use Router

    override def receive: Receive = {
      case message => Future {
        // wait on a resource
        Thread.sleep(5000)
        log.info(s"Success: $message")
      }
    }
  }

  val system = ActorSystem("DispatcherConfigDemo")
  val dbActor = system.actorOf(Props[DBActor])
    //dbActor ! "the meaning of life is 42"

  val nonblockingActor = system.actorOf(Props[Counter])
  for (i <- 1 to 1000) {
    val message = s"important message $i"
    dbActor ! message
    nonblockingActor ! message
  }
}

object jj extends App {
  var a  = 0
  for(a <- 1 until 5) {
    println(a)
  }
}