package com.aamir.akka

import akka.actor.{Actor, ActorSystem, Props}


class ResolveActorByString extends Actor {
  override def receive: Receive = {
    case x: String => println(s"from actor: $x")
  }
}

object MainTest extends App {

  val as = ActorSystem("testresolveactorbyname")
  val className = "com.aamir.akka.ResolveActorByString"
  val dd = Class.forName(className).asInstanceOf[Class[Actor]]
  val actorRef = as.actorOf(
    Props.apply(Class.forName(className).asInstanceOf[Class[Actor]]), "ff"
  )

   actorRef ! "hello"
}