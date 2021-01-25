package com.aamir.akka.actors.faulttolerance


import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.actor.{Actor, ActorRef, ActorSystem, AllForOneStrategy, OneForOneStrategy, Props, SupervisorStrategy, Terminated}
import akka.testkit.{EventFilter, ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll}
import org.scalatest.wordspec.AnyWordSpecLike

/**
 * Parent must decide upon their children's failure
 * When an actor fails,
 * it suspends its children
 * sends a special message to its parent
 * Parent decide to Restart, Resume, or Stop child or Escalate to its parent and fail itself.
 *
 * When an actor escalates, it stops all its children and escalates error to its parent i.e user-guardian, and user-guardian restarts parents and children are killed.
 */
class SupervisionSpec extends TestKit(ActorSystem("SupervisorSpec"))
  with ImplicitSender with AnyWordSpecLike with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  import SupervisionSpec._

  "A supervisor" should {
    "resume its child in case of a minor fault" in {
      val supervisor = system.actorOf(Props[Supervisor], "supervisor")
      supervisor ! Props[FussyWordCounter]
      val child = expectMsgType[ActorRef]

      child ! "I love akka"
      child ! Report
      expectMsg(3)

      child ! "Akka is awesome, because I've started to think in a whole different way" // throws RuntimeException, gets Resumed
      child ! Report
      expectMsg(3)
    }

    "restarts its child in case of an empty sentence" in {
      val supervisor = system.actorOf(Props[Supervisor], "supervisor")
      supervisor ! Props[FussyWordCounter]
      val child = expectMsgType[ActorRef]

      child ! "I love akka"
      child ! Report
      expectMsg(3)

      child ! ""
      child ! Report
      expectMsg(0)
    }

    "terminate its child in case of a major error" in {
      val supervisor = system.actorOf(Props[Supervisor], "supervisor")
      supervisor ! Props[FussyWordCounter]
      val child = expectMsgType[ActorRef]
      watch(child)

      child ! "akka is nice"

      val terminatedRef = expectMsgType[Terminated]
      assert(terminatedRef.actor == child)

    }

    /**
     * When an actor escalates, it stops all its children and escalates error to its parent i.e user-guardian, and user-guardian restarts parents only not children.
     * In this scenario error is thrown in supervisor actor because it got escalated to user guardian, for rest it is thrown in respective child actors.
     * [ERROR] [02/22/2020 16:38:34.752] [SupervisorSpec-akka.actor.default-dispatcher-4] [akka://SupervisorSpec/user/supervisor] only string are allowed
     * Mostly error will be thrown in actor which is about to get restarted mostly in the respective child actor
     */

    "escalates an error when it doesn't know what to do" in {
      //
      val supervisor = system.actorOf(Props[Supervisor], "supervisor")
      supervisor ! Props[FussyWordCounter]
      val child = expectMsgType[ActorRef]
      watch(child)

      child ! 43
      val terminatedRef = expectMsgType[Terminated]
      assert(terminatedRef.actor == child)
    }
  }

  /**
   * When an actor escalates, it stops all its children and escalates error to its parent i.e user-guardian, and user-guardian restarts parents only not children,
   * but here in NoDeathOnRestartSupervisor we are overriding preReStart means, children are not killed but restarted too.
   */
  "A kinder supervisor" should {
    "not kill children in case it's restarted or escalates failure" in {
      val supervisor = system.actorOf(Props[NoDeathOnRestartSupervisor])
      supervisor ! Props[FussyWordCounter]
      val child = expectMsgType[ActorRef]

      watch(child)
      child ! "Akka is cool"
      child ! Report
      expectMsg(3)

      child ! 43
      child ! Report
      expectMsg(0)
    }
  }

  "All for one supervisor" should {
    "apply all for one strategy" in {
      val supervisor = system.actorOf(Props[AllForOneSupervisor])

      supervisor ! Props[FussyWordCounter]
      val child1 = expectMsgType[ActorRef]

      supervisor ! Props[FussyWordCounter]
      val child2 = expectMsgType[ActorRef]

      child2 ! "All for one"
      child2 ! Report
      expectMsg(3)

      //waits for child1 to throw NullPointerException
      EventFilter[NullPointerException]() intercept {
        child1 ! ""
      }

      Thread.sleep(500)
      child2 ! Report
      expectMsg(0)

    }
  }


}

object SupervisionSpec {

  class Supervisor extends Actor {

    override val supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
      case _: IllegalArgumentException => Stop
      case _: NullPointerException     => Restart
      case _: RuntimeException         => Resume
      case _: Exception                => Escalate

    }

    override def receive: Receive = {
      case props: Props => {
        val childActorRef = context.actorOf(props)
        sender() ! childActorRef
        context.watch(childActorRef)
      }
    }
  }

  class NoDeathOnRestartSupervisor extends Supervisor {
    override def preRestart(reason: Throwable, message: Option[Any]): Unit = {

    }
  }

  class AllForOneSupervisor extends Supervisor {
    override val supervisorStrategy = AllForOneStrategy() {
      case _: NullPointerException     => Restart
      case _: RuntimeException         => Resume
      case _: IllegalArgumentException => Stop
      case _: Exception                => Escalate
    }
  }

  case object Report

  class FussyWordCounter extends Actor {

    var words = 0

    override def receive: Receive = {
      case Report           => sender() ! words
      case ""               => throw new NullPointerException("sentence is empty")
      case sentence: String => {
        if (sentence.length > 20) throw new RuntimeException("sentence is lengthy")
        else if (!Character.isUpperCase(sentence(0))) throw new IllegalArgumentException("sentence must start with upper-case")
        else words += sentence.split(" ").length
      }
      case _                => throw new Exception("only string are allowed")
    }
  }

}