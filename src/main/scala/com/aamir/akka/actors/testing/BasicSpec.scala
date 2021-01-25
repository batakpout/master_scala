package com.aamir.akka.actors.testing


import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.duration._
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import org.scalatest.{BeforeAndAfterAll}

import scala.collection.immutable
import scala.util.Random
import org.scalatest.wordspec.AnyWordSpecLike

class BasicSpec extends TestKit(ActorSystem("BasicSpec"))
      with ImplicitSender
      with AnyWordSpecLike
      with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  import BasicSpec._

  //a should == Test Suite

  "An Echo actor" must {

    "send back messages unchanged" in {
      val echo = system.actorOf(TestActors.echoActorProps)
      echo ! "hello world"
      expectMsg("hello world")
    }
  }

  "A simple Actor" should {
    "send back the same message" in {
      val echoActor = system.actorOf(Props[SimpleActor])
      val message = "hello, test!"
      echoActor ! message
      expectMsg(message) //akka.test.single-expect-default
      testActor
    }
  }
  "A Black Actor" should {
    "send back some message" in {
      val blackActor = system.actorOf(Props[BlackHoleActor])
      val message = "hello, test!"
      blackActor ! message
      expectNoMessage(1.second) // till 1 second no message should come
    }
  }

  "A Lab test actor" should {
    val labTestActor = system.actorOf(Props[LabTestActor])
    "turn a message into uppercase" in {
      labTestActor ! "I love Akka!"
      //expectMsg("I LOVE AKKA!")
      val reply = expectMsgType[String]
      assert(reply == "I LOVE AKKA!")
    }

    "reply to a greeting" in {
      labTestActor ! "greeting"
      expectMsgAnyOf("hi", "hello", "bye")
    }

    "replay to a greeting with favorite tech" in {
      labTestActor ! "favoriteTech"
      expectMsgAllOf("Scala", "Akka")
    }

    "reply with cool tech in a different way" in {
      labTestActor ! "favoriteTech"
      val messages: immutable.Seq[AnyRef] = receiveN(2)
      //println(messages)
    }

    "reply with cool tech in a fancy way" in {
      labTestActor ! "favoriteTech"
      //expectMsgPF will check only the first message that will arrive matching it against the first or the second case
      expectMsgPF() {
        case "Scala" => // if "Scala" comes from actor and its not here then it will throw Runtime error....
        case "Akka" => //case "Akkaaaa" passes reason on line 79
      }
    }
  }
}

object BasicSpec {
  class SimpleActor extends Actor {
    override def receive: Receive = {
      case message => sender() ! message
    }
  }

  class BlackHoleActor extends Actor {
    override def receive: Receive = {
      case message => Actor.emptyBehavior
    }
  }

  class LabTestActor extends Actor {
    val random = new Random()
    override def receive: Receive = {
      case "greeting" => if(random.nextBoolean()) sender() ! "hi" else sender() ! "bye"
      case "favoriteTech" => {
        sender() ! "Scala"
        sender() ! "Akka"
      }
      case message: String => sender() ! message.toUpperCase()
    }
  }
}