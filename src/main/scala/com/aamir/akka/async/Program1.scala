package com.aamir.akka.async

import akka.actor.{Actor, ActorRef, Stash}
import akka.pattern.pipe

import scala.concurrent.Future

class MyActor extends Actor with Stash {

  import context.dispatcher
case class WrappedFuture(actorRef: ActorRef, result: String)
  case class DoThing(x: Int)
  // Save the correct sender ref by using something like
  // case class WrappedFuture(senderRef: ActorRef, result: Any)
  def doThing(): Future[WrappedFuture] = Future.successful(WrappedFuture(self, ""))

  override def receive: Receive = {
    case msg: DoThing =>
      doThing() pipeTo self
      context.become({
        case WrappedFuture(senderRef, result) =>
          senderRef ! result
          unstashAll()
          context.unbecome()
        case newMsg: DoThing =>
          stash()
      }, discardOld = false)
  }
}