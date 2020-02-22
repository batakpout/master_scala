package akka.actors.testing

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

class TestProbeSpec extends TestKit(ActorSystem("TestProbeSpecs"))
  with ImplicitSender with WordSpecLike with BeforeAndAfterAll {
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  import TestProbeSpec._

  "A master actor" should {
    "Register a slave" in {
      val master = system.actorOf(Props[Master])
      val slave = TestProbe("slave")
      master ! Register(slave.ref)
      expectMsg(RegistrationAck)
    }
  }
  "send the work to the slave actor" in {
    val master = system.actorOf(Props[Master])
    val slave = TestProbe("slave")
    master ! Register(slave.ref)
    expectMsg(RegistrationAck)

    val workLoadString = "I Love Akka"
    master ! Work(workLoadString)

    slave.expectMsg(SlaveWork(workLoadString, testActor))
    slave.reply(WorkCompleted(3, testActor))

    expectMsg(Report(3))
  }

  "aggregate data correctly" in {
    val master = system.actorOf(Props[Master])
    val slave = TestProbe("slave")
    master ! Register(slave.ref)
    expectMsg(RegistrationAck)

    val workLoadString = "I Love Akka"

    master ! Work(workLoadString)
    master ! Work(workLoadString)

    slave.receiveWhile() {
      case SlaveWork(`workLoadString`, `testActor`) => slave.reply(WorkCompleted(4, testActor))
    }

    expectMsg(Report(4))
    expectMsg(Report(8))
  }
}

object TestProbeSpec {

  // scenario
  /*
  word counting actor hierarchy master-slave
  send some work to the master
    - master sends the slave the piece of work
    - slave processes the work and replies to master
    - master aggregates the result
  master sends the total count to the original requester
 */
  case class Register(slaveRef: ActorRef)

  case class SlaveWork(text: String, originalRequester: ActorRef)

  case class WorkCompleted(count: Int, originalRequester: ActorRef)

  case class Work(text: String)

  case object RegistrationAck

  case class Report(totalCount: Int)

  class Master extends Actor {
    override def receive: Receive = {
      case Register(slaveRef) => {
        sender() ! RegistrationAck
        context.become(online(slaveRef, 0))
      }
      case _                  =>
    }

    def online(slaveRef: ActorRef, totalWordCount: Int): Receive = {
      case Work(text)                              => slaveRef ! SlaveWork(text, sender())
      case WorkCompleted(count, originalRequester) => {
        val newTotalWordCount = count + totalWordCount
        originalRequester ! Report(newTotalWordCount)
        context.become(online(slaveRef, newTotalWordCount))
      }
    }
  }

  // class Slave extends Actor ....

}