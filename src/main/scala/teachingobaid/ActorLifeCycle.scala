
import akka.actor.SupervisorStrategy.{Restart, Resume}
import akka.actor.{Actor, ActorSystem, OneForOneStrategy, Props}

object ActorSupervision1 extends App {

  case object CreateChild
  case class SignalChildren(order: Int)
  case class PrintSignal(order: Int)
  case class DivideByZero(n: Int, d: Int)
  case object BadStuff

  class ParentActor extends Actor {
    private var num = 0

    override def receive = {
      case CreateChild =>
        context.actorOf(Props[ChildActor], s"child-$num")
        num += 1
      case SignalChildren(n) =>
        context.children.foreach(_ ! PrintSignal(n))
    }

    override val supervisorStrategy: OneForOneStrategy = OneForOneStrategy(loggingEnabled = false) {
      case _: ArithmeticException => Resume // No LifeCycle methods will be called on Resume
      case _: Exception           => Restart
    }
  }

  class ChildActor extends Actor {

    println("From Default Constructor, Child Actor Created..")

    override def receive = {
      case PrintSignal(n) => s"from child ${self.path.name}, $n"
      case DivideByZero(n, d) => println(s"n /d is ${n / d}")
      case BadStuff           => throw new RuntimeException("Bad stuff happened")
    }

    override def preStart(): Unit = {
      super.preStart()
      println("From PreStart")
    }

    override def postStop(): Unit = {
      super.postStop()
      println("From postStop")
    }

    override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
      super.preRestart(reason, message)
      println("From PreRestart")
    }

    override def postRestart(reason: Throwable): Unit = {
      super.postRestart(reason)
      println("From PostRestart")
    }

  }

  val system = ActorSystem("ActorHierarchy1")
  val actor = system.actorOf(Props[ParentActor], "ParentActor1")

  actor ! CreateChild
  actor ! CreateChild
  actor ! CreateChild
  actor ! CreateChild
  actor ! CreateChild

  actor ! SignalChildren(2)

  val actSel = system.actorSelection("/user/ParentActor1/child-0")
  actSel ! DivideByZero(4, 0)
  //actSel ! DivideByZero(4, 2)
  Thread.sleep(1000)
  println("Nothing happens by above two statements, as Resume executes in supervisor strategy")
  println("Now, Restart actor when BadStuff happens")
  //actSel ! BadStuff

}
/**
 * one-for-one strategy, meaning that each child is treated separately (an all-for-one strategy works very similarly,
 * the only difference is that any decision is applied to all children of the supervisor, not only the failing one)
 */