package com.aamir.akka.actors.testing


import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll}
import org.scalatest.wordspec.AnyWordSpecLike

import scala.collection.immutable
import scala.concurrent.duration._
import scala.util.Random

/**
 * Put a time cap on assertions
 */

class TimedAssertionsSpec extends TestKit(ActorSystem("TimedAssertionsSpec", ConfigFactory.load().getConfig("specialTimedAssertionsConfig")))
  with ImplicitSender with AnyWordSpecLike with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  import TimedAssertionsSpec._

  "A worker actor" should {
    val workerActor = system.actorOf(Props[WorkActor])

    "reply with the meaning of life in a timely manner" in {
      within(600.millis, 1.second) { // boundary, min, max it should receive result
        workerActor ! "work"
        expectMsg(WorkResult(42))
      }
    }

    "check if certain messages are generated at a certain rate" in {
      within(1.millis) {
        workerActor ! "workSequence"
        val result: immutable.Seq[Int] = receiveWhile[Int](max = 2.second, idle = 500.millis, messages = 10) {
              //messages = 2, receive only 2 messages, skip others
          case WorkResult(result) => result
        }
        assert(result.sum > 5) // assert ignores within(1.millis)
      }
    }

    "reply to a test probe in timely manner" in {
      //time boxing
      within(1.second) { //100.millis fails, as block took 500 millis to execute exceeding 100 millis
        val probe = TestProbe("slave")
        probe.send(workerActor, "work") // here probe acts as sender()
        probe.expectMsg(WorkResult(42)) // timeout of 0.6 seconds in application.conf file overrides within(1.second)

      }
    }
  }
}

object TimedAssertionsSpec {

  case class WorkResult(result: Int)

  class WorkActor extends Actor {
    override def receive: Receive = {
      case "work"         => {
        //let's say some long computation taking some time
        Thread.sleep(500)
        sender() ! WorkResult(42)
      }
      case "workSequence" => {
        Thread.sleep(300)
        val r = new Random()
        for (_ <- 1 to 10) {
          Thread.sleep(r.nextInt(50))
          sender() ! WorkResult(1)
        }
      }
    }
  }

}
