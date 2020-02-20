package akka.actors.part1

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.actors.part1.ActorPart4.Parent.{CreateChild, TellChild}
import akka.actors.part1.NaiveBankAccountApp.CreditCard.{AttachToAccount, CheckStatus}

object ActorPart4 extends App {

  object Parent {
    case class CreateChild(name: String)
    case class TellChild(message: String)
  }
  class Parent extends Actor {
    import Parent._
    override def receive: Receive = {
      case CreateChild(name) => {
        println(s"${self.path}, creating child..")
        val childRef = context.actorOf(Props[Child], name)
        context.become(withChild(childRef))
      }
    }
    def withChild(childRef: ActorRef): Receive = {
      case TellChild(message) => childRef forward  message
    }
  }

  class Child extends Actor {
    override def receive: Receive = {
      case message => println(s"${self.path}, I got message: $message")
    }
  }

  val system = ActorSystem("ParentChildDemo")
  val parent = system.actorOf(Props[Parent], "parent")
  parent ! CreateChild("child-1")
  parent ! TellChild("Hey child....")

  val actorSel = system.actorSelection("/user/parent/child-1") //actorSelection works with context as well
  actorSel ! "Message, using actor selection to child"
  /* Every akka ActorSystem has 3 guardians
    Guardian (or supervisors)actors (top-level)
    - /system = system guardian
    - /user = user-level guardian
    - / = the root guardian
   */

}


/**
 * Danger!
 *
 * NEVER PASS MUTABLE ACTOR STATE, OR THE `THIS` REFERENCE, TO CHILD ACTORS.
 *
 * NEVER IN YOUR LIFE.
 *
 * something called as closing over mutable state or this reference
 * NEVER CLOSE OVER MUTABLE STATE OR this
 */

object NaiveBankAccountApp extends App {

  object NaiveBankAccount {
    case class Deposit(amount: Int)
    case class Withdraw(amount: Int)
    case object InitializeAccount
  }

  object CreditCard {
    case class AttachToAccount(bankAccount: NaiveBankAccount)
    case object CheckStatus
  }

  class NaiveBankAccount extends Actor {
    var amount = 0
    import CreditCard._
    import NaiveBankAccount._
    override def receive: Receive = {
      case InitializeAccount => {
        val creditCardRef = context.actorOf(Props[CreditCard], "creditcard")
        creditCardRef ! AttachToAccount(this)
      }
      case Deposit(amount) => deposit(amount)
      case Withdraw(amount) => withDraw(amount)
    }
    def deposit(funds: Int) = {
      println(s"${self.path} depositing $funds on top of $amount")
      amount += funds
    }
    def withDraw(funds: Int) = {
      println(s"${self.path} withdrawing $funds from $amount")
      amount -= funds
      Thread.sleep(10000)
    }
  }

  class CreditCard extends Actor {
    override def receive: Receive = {
      case AttachToAccount(account) => context.become(attachedTo(account))
    }
    def attachedTo(bankAccount: NaiveBankAccount): Receive = {
      case CheckStatus => {
        println(s"${self.path}, your message has been processed..")
        bankAccount.withDraw(100)
      }
    }
  }

  import NaiveBankAccount._
  val system = ActorSystem("NaiveBankAccountDemo")
  val bankAccountRef = system.actorOf(Props[NaiveBankAccount], "NaiveBankAccount")
  bankAccountRef ! InitializeAccount
  bankAccountRef ! Deposit(100)
  val actorSel = system.actorSelection("/user/NaiveBankAccount/creditcard")
  actorSel ! CheckStatus
}

object ChildActorExercise extends App {

  object WordCounterMaster {
    case class Initialize(nChildren: Int)
    case class WordCountTask(id:Int, text: String)
    case class WordCountReply(id:Int, count: Int)
  }
  import WordCounterMaster._
  class WordCounterMaster extends Actor {
    override def receive: Receive = {
      case Initialize(nChildren) => {
        println("[master] initializing....")
        val childRefsSeq = for(i <- 0 until nChildren) yield context.actorOf(Props[WordCounterWorker], s"wcw_$i")
        context.become(withChildren(childRefsSeq, 0, 0, Map.empty))
      }
    }

    def withChildren(childrenRefs: Seq[ActorRef], currentChildIndex:Int, currentTaskId: Int,
                     requestMap:Map[Int, ActorRef]): Receive = {
      case text: String => {
        println(s"[master]: I've received text: $text, I'll send it to: child-$currentChildIndex")
        val originalSender = sender()
        val childRef = childrenRefs(currentChildIndex)
        childRef ! WordCountTask(currentTaskId, text)
        val newChildIndex = (currentTaskId + 1) % childrenRefs.length // list.tail +: list.head
        val newRequestMap = requestMap + (currentTaskId -> originalSender)
        context.become(withChildren(childrenRefs, newChildIndex, currentTaskId + 1, newRequestMap))
      }
      case WordCountReply(id, count) => {
        println(s"[master]: I've have a reply for task: $id, with count:$count")
        val originalSender = requestMap(id)
        originalSender ! count
        context.become(withChildren(childrenRefs, currentChildIndex, currentTaskId , requestMap - id))
      }
    }
  }

  class WordCounterWorker extends Actor {
    import WordCounterMaster._
    override def receive: Receive = {
      case WordCountTask(id, text) => {
        println(s"[worker]: ${self.path}, I've received task:$id with text: $text")
        sender() ! WordCountReply(id, text.split(" ").length)
      }
    }
  }

  class TestMasterWorker extends Actor {
    override def receive: Receive = {
      case "go" => {
        val master = context.actorOf(Props[WordCounterMaster], "master")
        master ! Initialize(10)
        val texts = List("I love Scala", "akka rocks", "Scala is like dope", "akka streams about to learn",
          "Hey how r you this morning", "the matrix of the nationality wide spams", "The quick white lion jumped over the lazy deer", "Yoda to the mouda fanks",
          "Hey the funcitionaly is not working at all", "major replase in the contents of the workd", "shiny world teweets of the world", "humble quick qurik of the",
          "my rants, my benign requests")
        texts.foreach{ master ! _}
      }
      case count: Int =>
        println(s"[testmasterworker]: I received a reply: $count")
    }

  }

  val system = ActorSystem("roundRobinWordCountExercise")
  val testActor = system.actorOf(Props[TestMasterWorker], "TestMasterWorker")
  testActor ! "go"

  /*
    create WordCounterMaster
    send Initialize(10) to wordCounterMaster
    send "Akka is awesome" to wordCounterMaster
      wcm will send a WordCountTask("...") to one of its children
        child replies with a WordCountReply(3) to the master
      master replies with 3 to the sender.
    requester -> wcm -> wcw
           r  <- wcm <-
   */
  // round robin logic
  // 1,2,3,4,5 and 7 tasks
  // 1,2,3,4,5,1,2

}