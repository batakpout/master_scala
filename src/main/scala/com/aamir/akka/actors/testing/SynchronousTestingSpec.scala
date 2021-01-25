package com.aamir.akka.actors.testing

import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.{CallingThreadDispatcher, TestActorRef, TestProbe}
import org.scalatest.{BeforeAndAfterAll}
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.duration._

/**
 * Synchronous testing...
 * all messages are handled in the calling thread...using
 * 1. TestActorRef
 * 2. Calling Thread Dispatcher
 */
class SynchronousTestingSpec extends AnyWordSpecLike with BeforeAndAfterAll {

  implicit val system = ActorSystem("SynchronousTestingSpec")

  override def afterAll(): Unit = {
    system.terminate()
  }

  import SynchronousTestingSpec._

  "A Counter" should {
    "Synchronously increase its counter" in {
      val counter = TestActorRef[Counter](Props[Counter])
      counter ! Read
      assert(counter.underlyingActor.count == 0)
      counter ! Inc //counter has already received the message,
      //becoz sending a message to TestActorRef happens in the calling thread
      assert(counter.underlyingActor.count == 1)
    }

    "synchronously increase its counter at the call of the receive function" in {
      val counter = TestActorRef[Counter](Props[Counter])
      counter.receive(Inc)
      assert(counter.underlyingActor.count == 1)
    }

    "work on the calling thread dispatcher" in {
      val counter = system.actorOf(Props[Counter].withDispatcher(CallingThreadDispatcher.Id))
      val probe = TestProbe()
      probe.send(counter, Read) // due to the fact that counter operates on calling thread dispatcher,
      // after this line i.e line probe.send(counter, Read) probe has already received count reply becoz of dispatcher above
      probe.expectMsg(Duration.Zero, 0) //probe will have already received message 0, becoz line 35 has already happened,
      //so Duration.Zero will have no effect, i.e it should not take any timeout at all
    }

    "work without the calling thread dispatcher" in {
      val counter = system.actorOf(Props[Counter])
      val probe = TestProbe()
      probe.send(counter, Read)
      probe.expectMsg(Duration.Zero, 0) // now counter operates in asynchronous manner
    }
  }
}

object SynchronousTestingSpec {
  case object Inc
  case object Read

  class Counter extends Actor {
    var count = 0

    override def receive: Receive = {
      case Inc => count += 1
      case Read => sender() ! count
    }
  }
}