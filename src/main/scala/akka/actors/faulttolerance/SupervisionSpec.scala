package akka.actors.faulttolerance

import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.actor.{Actor, ActorRef, ActorSystem, OneForOneStrategy, Props, SupervisorStrategy}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}
/**
 * Parent must decide upon their children's failure
 * When an actor fails,
 * it suspends its children
 * sends a special message to its parent
 * Parent decide to Restart, Resume, or Stop child or Escalate to its parent and fail itself.
 */
class SupervisionSpec extends TestKit(ActorSystem("SupervisorSpec"))
  with ImplicitSender with WordSpecLike with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  import SupervisionSpec._

  "A supervisor" should {
    "resume its child in case of a minor fault" in {
      val supervisor = system.actorOf(Props[Supervisor], "supervisor")
      //supervisor ! Props[FussyWordCounter]
      //val child = expectMsgType[ActorRef]

      //child ! "I love akka"
      //child ! Report
      //expectMsg(3)

     /* child ! "Akka is awesome, because I've started to think in a whole different way" // throws RuntimeException, gets Resumed
      child ! Report
      expectMsg(3)*/
    }
  }

  object SupervisionSpec {

    class Supervisor extends Actor {

      override val supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {

        case _: NullPointerException     => Restart
        case _: RuntimeException         => Resume
        case _: IllegalArgumentException => Stop
        case _: Exception                => Escalate

      }

      override def receive: Receive = {
        case props: Props => {
          val childActorRef = context.actorOf(props)
          sender() ! childActorRef
        }
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

}