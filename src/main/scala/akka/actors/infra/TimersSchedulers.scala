package akka.actors.infra

import akka.actor.{Actor, ActorLogging, ActorSystem, Cancellable, Props, Timers}

import scala.concurrent.duration._

object TimersSchedulers extends App {

  class SimpleActor extends Actor with ActorLogging {

    override def receive: Receive = {
      case message => log.info(s"message : ${message.toString}")
    }
  }

  val system = ActorSystem("SchedulersTimersDemo")
  val simpleActor = system.actorOf(Props[SimpleActor])

  system.log.info("Scheduling reminder for SimpleActor")


  //import system.dispatcher
  //implicit val dispatcher = system.dispatcher

  system.scheduler.scheduleOnce(4.second) {
    simpleActor ! "reminder"
  }(system.dispatcher)

  implicit val dispatcher = system.dispatcher
  val routine = system.scheduler.schedule(1.second, 2.seconds) {
    simpleActor ! "heart beart"
  }

  system.scheduler.scheduleOnce(10.second) {
    routine.cancel()
  }
}

object TimeSchedulers2 extends App {

  /**
   * If the actor receives a message(anything), you have 1 second to send it another message
   * if the time window expires, the actor will stop itself
   * if u second another message before this 1 second window, the time window is reset to 1 second again
   **/

  import scala.concurrent.ExecutionContext.Implicits.global

  class SelfClosingActor extends Actor with ActorLogging {

    var schedule: Cancellable = createTimeoutWindow()

    def createTimeoutWindow() = {
      context.system.scheduler.scheduleOnce(2.second) {
        self ! "timeout"
      }
    }

    override def receive: Receive = {

      case "timeout" => {
        log.info("Stopping myself")
        context.stop(self)
      }
      case message   => {
        log.info(s"Received $message, staying alive")
        schedule.cancel()
        schedule = createTimeoutWindow()
      }
    }

  }

  val system = ActorSystem("SchedulersTimersDemo")

  val selfClosingActor = system.actorOf(Props[SelfClosingActor], "selfclosingactor")

  system.scheduler.scheduleOnce(250.millis) {
    selfClosingActor ! "ping"
  }

  system.scheduler.scheduleOnce(2.seconds) {
    system.log.info(s"sending pong to the self-closing actor")
    selfClosingActor ! "pong"
  }

}

object TimersSchedulers3 extends App {

  /**
   * Timer
   **/

  case object TimerKey

  case object Stop

  case object Start

  case object Reminder

  class TimeBasedHeartbeatActor extends Actor with ActorLogging with Timers {

    timers.startSingleTimer(TimerKey, Start, 500.millis)

    override def receive: Receive = {
      case Start    => {
        log.info("Bootstrapping")
        timers.startPeriodicTimer(TimerKey, Reminder, 1.second)
      }
      case Reminder => {
        log.info("I'm alive")
      }
      case Stop     => {
        log.warning("Stopping")
        timers.cancel(TimerKey)
        context.stop(self)
      }
    }
  }

  val system = ActorSystem("SchedulersTimersDemo")

  val timerHeartbeatActor = system.actorOf(Props[TimeBasedHeartbeatActor], "timerActor")
  system.scheduler.scheduleOnce(10.seconds) {
    timerHeartbeatActor ! Stop
  }(system.dispatcher)

}

object TimersTest extends App {

  class ActorA extends Actor with Timers {

    timers.startPeriodicTimer(
      "myptimer",
      "Hi",
      2.seconds
    )

    def receive: Receive = {
      case "Hi" => println("timer called")
      case _    => println("_____")
    }
  }

  val system = ActorSystem("as")
  val actorRef = system.actorOf(Props[ActorA], "actora")


}