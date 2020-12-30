package com.aamir.akka.actors

import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props, Terminated}

import java.util.UUID
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}
import scala.concurrent.duration.DurationInt

object TerminateActorTest extends App {

  case class CreateActor(str: String)

  class Parent extends Actor {

     context.watch(self)
    override def receive: Receive = {
      case CreateActor(name) => {
        println(s"my path: ${self.path}")
      }
      case "divide" => {
        11 / 0
      }
      case _ => println("ne pas toucher la lintelel")

    }

    override def postStop(): Unit = {
      super.postStop()
      println(s"--------I got killed daleee---------${self.path}")
    }
  }

  val system = ActorSystem("ParentChildDemo")
  val name1 = s"parent1-${UUID.randomUUID().toString}"
  val name2 = s"parent2-${UUID.randomUUID().toString}"

  println(name1)
  println(name2)


  println("*" * 50)

  val parent1: ActorRef = system.actorOf(Props[Parent], name1)
  val parent2: ActorRef = system.actorOf(Props[Parent], name2)


  println(parent1.path.name)
  println(parent2.path.name)
  println(parent2.path)



/*  val path1 = parent1.path.toString
  val path2 = parent2.path.toString

  val actorSelection1 = system.actorSelection(path1)
  val actorSelection2 = system.actorSelection(path2)

  implicit val ex: ExecutionContext = system.dispatcher
  system.scheduler.schedule(4.seconds, 6.seconds) {
    actorSelection1 ! PoisonPill
  }

  actorSelection2 ! "divide"*/


}