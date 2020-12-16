package com.aamir.akka.actors.faulttolerance

import akka.actor.{Actor, ActorLogging, ActorSystem, PoisonPill, Props}

/**
 * Start = create actorRef with UUID
 * suspend = the actorRef will enqueue but NOT process more messages, opposite of Resume, suspend then Resume simply
 * Restarting is trickier:
 *1. SWAP -- suspend will happen then 2. old ref calls preRestart, 3. new actorRef created calls postRestart 4. Resume
 * internal state is destroyed on restart
 * *
 * Stopping = calls postStop , all death watch(all watching actors) will receive terminated message
 * Stopping frees the actorRef within the path, so enqueued messages wont be processed anymore
 * Stopping means stopping all children recursively first then the parent
 * finally actor will shutdown
 * *
 * By default supervision strategy is Restart, i.e the actor msg that caused error throw will be removed from queue
 * and not put in mailbox again
 */
object LifeCycle extends App {

  case object StartChild

  class LifeCycleActor extends Actor with ActorLogging {

    override def preStart(): Unit = println("I am Starting...")

    override def postStop(): Unit = println("I am Stopping...")

    override def receive: Receive = {
      case StartChild => {
        context.actorOf(Props[LifeCycleActor], "child")
      }
    }
  }

    val system = ActorSystem("LifeCycleDemo")
  //  val parent = system.actorOf(Props[LifeCycleActor],"parent")
  //  parent ! StartChild
  //  parent ! PoisonPill // first child will stop before parent
  //
  //  parent ! StartChild // to deadletters

  /**
   * restart
   */
  case object Fail

  case object Check

  case object FailChild

  case object CheckChild

  class Parent extends Actor with ActorLogging {

    val childRef = context.actorOf(Props[Child], "supervisedActor")

    override def receive: Receive = {
      case FailChild  => childRef ! Fail
      case CheckChild => childRef ! Check
    }
  }

  class Child extends Actor with ActorLogging {
    override def preStart(): Unit = println("supervised child started...")

    override def postStop(): Unit = println("I am Stopping...") //called only when actor stopped , not when its restarted

    override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
      super.preRestart(reason, message)
      println(s"supervised actor restarting because of ${reason.getMessage}...")
    }

    override def postRestart(reason: Throwable): Unit = {
      super.postRestart(reason)
      println(s"supervised actor restarted...message = ${reason.getMessage}")
    }

    override def receive: Receive = {
      case Fail  => {
        println("child will fail now...")
        throw new RuntimeException("I failed...")
      }
      case Check => {
        println("alive and kicking...")
      }
    }
  }

  val supervisor = system.actorOf(Props[Parent], "supervisor")
  supervisor ! FailChild
  supervisor ! CheckChild
}
