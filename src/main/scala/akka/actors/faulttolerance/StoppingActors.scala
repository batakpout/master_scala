package akka.actors.faulttolerance

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Kill, PoisonPill, Props, Terminated}
import akka.actors.faulttolerance.StoppingActors.Parent.{StartChild, StopChild}

/**
 * Ways to stop actors,
 * 1. context.stop()
 * context.stop(child), asynchronous operation so child actor may continue to receive messages until actually stopped.
 * context.stop(self), will recursively stop children (asynchronously)
 *
 * 2. using special messages
 * actor ! PoisonPill
 * actor ! Kill, makes actor throw exception: ActorKilledException
 *
 * Death Watch, context.watch(actor)
 */

object StoppingActors extends App {

  object Parent {
    case class StartChild(name: String)
    case class StopChild(name: String)
    case object Stop
  }
  import Parent._
  class Parent extends Actor with ActorLogging {
    override def receive: Receive = withChildren(Map())
    def withChildren(children: Map[String, ActorRef]): Receive = {
      case StartChild(name) => {
        log.info(s"starting child => $name")
        context.become(withChildren(
          children + (name -> context.actorOf(Props[Child], name))
        ))
      }
      case StopChild(name) => {
        log.info(s"stopping child => $name")
         children.get(name).foreach(context.stop)
        //childOption.foreach(childRef => context.stop(childRef))
      }
      case Stop => {
        log.info(s"stopping myself name => ${self.path.name}")
        context.stop(self)
      }
    }
  }
  class Child extends Actor with ActorLogging {
    override def receive: Receive = {
      case msg => log.info(msg.toString)
    }
  }

  /**
   * method #1 - using context.stop
   */

  val system = ActorSystem("StartingStoppingActors")

  val parent = system.actorOf(Props[Parent], "parent")

  //parent ! StartChild("child1")
  //val child1Ref = system.actorSelection("/user/parent/child1")
 // child1Ref ! "hi child1.."
 // parent ! StopChild("child1")
  //for(_ <- 1 to 50) child1Ref ! "child1, are you still there?"

  //parent ! StartChild("child2")
  //Thread.sleep(500)
  //val child2Ref = system.actorSelection("/user/parent/child2")
  //child2Ref ! "hi child2.."

  //parent ! Stop // this is message A
 // for(_ <- 1 to 50) parent ! "parent, are you still there?" // this wont be logged, as message A will go before B, A=kill parent
  //for(i <- 1 to 50) child2Ref ! s"[$i] second kid, are you still there?"

  /**
   * method #2 - using special messages
   */

//  val looseActor = system.actorOf(Props[Child], "looseChild")
//  looseActor ! "Hey loose actor!"
//  looseActor ! PoisonPill
//  looseActor ! "loose actor, r u still there?"
//
//  val abruptlyTerminatedActor = system.actorOf(Props[Child],"AbruptlyTerminatedActor")
//  abruptlyTerminatedActor ! "you are about to be terminated"
//  abruptlyTerminatedActor ! Kill // throws ActorKilledException and triggers Supervision
//  abruptlyTerminatedActor ! "you are terminated"

  class Watcher extends Actor with ActorLogging {
    override def receive: Receive = {
      case StartChild(name) => {
        val child = context.actorOf(Props[Child], name)
        log.info(s"started and watching child => $name")
        context.watch(child)
      }
      case Terminated(ref) => {
        log.info(s"the ref that i was watching => ${ref.path} has been stopped")
      }
    }
  }
  val watcherActorRef = system.actorOf(Props[Watcher], "watcheractor")
  watcherActorRef ! StartChild("child")

  val watchedChild = system.actorSelection("/user/watcheractor/child")
  Thread.sleep(500)
  watchedChild ! PoisonPill

}