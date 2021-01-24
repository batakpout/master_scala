package com.aamir.akka.actors.faulttolerance
import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, OneForOneStrategy, Props}
import akka.pattern.{Backoff, BackoffOpts, BackoffSupervisor, ask}

import scala.concurrent.Future
import scala.concurrent.duration._

/**
 * Pain: the repeated restarts of actors
 * -> restarting immediately might be useless
 * -> everyone attempting at the same time might kill the resources again
 * Create backoff supervision for exponential delays between attempts
 */
object BackoffSupervisorPattern extends App {

  case object ReadFile

  class FileBasedPersistentActor extends Actor with ActorLogging {
    var dataSource: scala.io.Source = null

    override def preStart(): Unit = {
      log.info("Persistent actor has started")
    }

    override def postStop(): Unit = {
      log.info("Persistent actor has stopped")
    }

     override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
       super.preRestart(reason, message)
       log.info("Persistent actor has pre restarted")
     }

     override def postRestart(reason: Throwable): Unit = {
       super.postRestart(reason)
       log.info("Persistent actor has post restarted")
     }

    override def receive: Receive = {
      case ReadFile => {
        if (dataSource == null) {
          dataSource = scala.io.Source.fromFile(new java.io.File("/home/aamir/code/udemy/src/main/resources/testfiles/important_data"))
          log.info(s"I've read some important data: ${dataSource.getLines.toList}")
        }
      }
      case msgWhileStopped: String => log.warning(msgWhileStopped)
    }
  }

  val system = ActorSystem("BackoffSupervisorDemo")
  //val simpleActor = system.actorOf(Props[FileBasedPersistentActor], "simpleActor")
  //simpleActor ! ReadFile // simply fails, and default supervisorstrategy applies, i.e RESTART the actor, i.e in which actor error occured, i.e child actor "simpleactor" here

  //Now, we will apply a Back-off Supervision pattern to handle exponential delay based restarts

  val simpleSupervisorProps: Props = BackoffSupervisor.props(
    BackoffOpts.onFailure(
      Props[FileBasedPersistentActor],
      "simpleBackoffActor",
      6.seconds, // first attempt is 3s then 6s, 12s, 24s
      30.seconds,
      0.2
    ).withSupervisorStrategy(
      OneForOneStrategy() {
        case _ => Restart //kicks-off this minBackOff restart, its default behaviour
      }
    )
  )
  //val simpleBackoffSupervisor = system.actorOf(simpleSupervisorProps, "simpleSupervisor") // simply calls preStart()
  //val simpleBackoffSupervisor = system.actorOf(Props[FileBasedPersistentActor], "simpleSupervisor") //started child, error, (restarts child) i.e stops child, restart, starting child again
 // simpleBackoffSupervisor ! ReadFile
  /*
    simpleSupervisor
      - child called simpleBackoffActor (props of type FileBasedPersistentActor)
      - supervision strategy is the default one (restarting on everything) i.e it uses FileBasedPersistentActor supervisor strategy i.e restart default after 3 seconds
        - first attempt after 3 seconds
        - next attempt is 2x the previous attempt
   */

  val stopSupervisorProps = BackoffSupervisor.props(
    Backoff.onStop( // start this actor after Stop directive after 3s
      Props[FileBasedPersistentActor],
      "stopBackoffActor",
      6 seconds,
      30 seconds,
      0.2
    ).withSupervisorStrategy(
      OneForOneStrategy() {
        case _ => Stop
      }
    ).withReplyWhileStopped("Not Ready!")
  )

   val stopSupervisor: ActorRef = system.actorOf(stopSupervisorProps, "stopSupervisor")
    stopSupervisor ! ReadFile

  Thread.sleep(2000)
  println("as actor is still in stopped state, lets see what is responds with")

  import akka.util.Timeout
  import scala.concurrent.ExecutionContext.Implicits.global
  implicit val timeOut = Timeout(4.seconds)
  val x: Future[String] = (stopSupervisor ? ReadFile).mapTo[String]
  x.onComplete(println)

  class EagerFBPActor extends FileBasedPersistentActor {
    override def preStart(): Unit = {
      log.info("Eager actor starting")
      dataSource = scala.io.Source.fromFile(new java.io.File("/home/aamir/code/udemy/src/main/resources/testfiles/important_data"))
    }

  }

  val repeatedSupervisorProps = BackoffSupervisor.props(
    Backoff.onStop(
      Props[EagerFBPActor],
      "eagerActor",
      3.second,
      30.seconds,
      0.1
    )
  )

    //val repeatedSupervisor = system.actorOf(Props[EagerFBPActor], "eagerSupervisor")
  /*
   default supervisor strategy for akka.actor.ActorInitializationException is Stop
   */
   // val repeatedSupervisor = system.actorOf(repeatedSupervisorProps, "eagerSupervisor")
}


object ParentHandlingSlaves {
  case class StartChild(name: String)

  class Parent extends Actor {
    override def receive: Receive = {
      case StartChild(name) => context.actorOf(Props[Child], name)
    }
  }

  class Child extends Actor {

    override val supervisorStrategy = OneForOneStrategy() {
      case _ => Escalate
    }

    override def receive: Receive = {
      case _ =>
    }
  }

  val back = BackoffSupervisor.props(
    Backoff.onStop(
      Props[Parent],
      "parent",
      3 seconds,
      30 seconds,
      0.2
    ).withSupervisorStrategy(
      OneForOneStrategy() {
        case _ => Stop
      }
    )
  )
}
