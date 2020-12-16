package com.aamir.akka.actors.part1
import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object ActorPart3 extends App {

  object FussyKid {

    case object KidAccept

    case object KidReject

    val HAPPY = "happy"
    val SAD = "sad"
  }

  class FussyKid extends Actor {

    import FussyKid._
    import Mom._

    var state = HAPPY

    override def receive: Receive = {
      case Food(VEGETABLE) => state = SAD
      case Food(CHOCOLATE) => state = HAPPY
      case Ask(_)          => if (state == HAPPY) {
        sender() ! KidAccept
      } else {
        sender() ! KidReject
      }
    }
  }

  class Mom extends Actor {

    import FussyKid._
    import Mom._

    override def receive: Receive = {
      case MomStart(kidRef) => {
        kidRef ! Food(VEGETABLE)
        kidRef ! Ask("Do you want to play!")
        kidRef ! Food(CHOCOLATE)
        kidRef ! Ask("Do you want to play!")
      }
      case KidAccept        => println("Yay, my kid is happy!")
      case KidReject        => println("My kid is sad but at least he is healthier!")
    }
  }

  object Mom {

    case class MomStart(kidRef: ActorRef)

    case class Food(food: String)

    case class Ask(message: String)

    val VEGETABLE = "veggies"
    val CHOCOLATE = "chocolate"
  }

  val system = ActorSystem("ChangingBehaviourDemo")
  val momRef = system.actorOf(Props[Mom])
  val kidRef = system.actorOf(Props[FussyKid])

  import Mom._

  momRef ! MomStart(kidRef)
}

/** *
 * to get rid of actor's mutable state, we change actor's behaviour
 */
object ImprovedActorPart3 extends App {

  object FussyKid {

    case object KidAccept

    case object KidReject

    val HAPPY = "happy"
    val SAD = "sad"
  }

  object Mom {

    case class MomStart(kidRef: ActorRef)

    case class Food(food: String)

    case class Ask(message: String)

    val VEGETABLE = "veggies"
    val CHOCOLATE = "chocolate"
  }

  class Mom extends Actor {

    import FussyKid._
    import Mom._

    override def receive: Receive = {
      case MomStart(kidRef) => {
        kidRef ! Food(VEGETABLE)
        kidRef ! Food(VEGETABLE)
        kidRef ! Ask("Do you want to play!")
        kidRef ! Food(CHOCOLATE)
        kidRef ! Food(VEGETABLE)
        kidRef ! Ask("Do you want to play!")
      }
      case KidAccept        => println("Yay, my kid is happy!")
      case KidReject        => println("My kid is sad but at least he is happy!")
    }
  }

  class StatelessFussyKid extends Actor {

    import FussyKid._
    import Mom._

    override def receive: Receive = happyReceive

    def happyReceive: Receive = {
      case Food(VEGETABLE) => context.become(sadReceive)
      case Food(CHOCOLATE) =>
      case Ask(_)          => sender() ! KidAccept
    }

    def sadReceive: Receive = {
      case Food(VEGETABLE) =>
      case Food(CHOCOLATE) => context.become(happyReceive)
      case Ask(_)          => sender() ! KidReject
    }
  }

  val system = ActorSystem("ChangingBehaviourDemo")
  val momRef = system.actorOf(Props[Mom])
  val kidRef = system.actorOf(Props[StatelessFussyKid])

  import Mom._

  momRef ! MomStart(kidRef)


}

object ImprovedActorPart3Stack extends App {

  object FussyKid {

    case object KidAccept

    case object KidReject

    val HAPPY = "happy"
    val SAD = "sad"
  }

  object Mom {

    case class MomStart(kidRef: ActorRef)

    case class Food(food: String)

    case class Ask(message: String)

    val VEGETABLE = "veggies"
    val CHOCOLATE = "chocolate"
  }

  class Mom extends Actor {

    import FussyKid._
    import Mom._

    override def receive: Receive = {
      case MomStart(kidRef) => {
        kidRef ! Food(VEGETABLE)
        kidRef ! Food(VEGETABLE)
        kidRef ! Food(CHOCOLATE)
        kidRef ! Ask("Do you want to play!")
      }
      case KidAccept        => println("Yay, my kid is happy!")
      case KidReject        => println("My kid is sad but at least he is happy!")
    }
  }

  class StatelessFussyKid extends Actor {

    import FussyKid._
    import Mom._

    override def receive: Receive = happyReceive

    def happyReceive: Receive = {
      case Food(VEGETABLE) => context.become(sadReceive)
      case Food(CHOCOLATE) =>
      case Ask(_)          => sender() ! KidAccept
    }

    def sadReceive: Receive = {
      case Food(VEGETABLE) => context.become(sadReceive, false) // push a sad handler on top of sad handler
      case Food(CHOCOLATE) => context.unbecome() // pop current handler from stack
      case Ask(_)          => sender() ! KidReject
    }
  }

  val system = ActorSystem("ChangingBehaviourDemo")
  val momRef = system.actorOf(Props[Mom])
  val kidRef = system.actorOf(Props[StatelessFussyKid])

  import Mom._

  momRef ! MomStart(kidRef)

  /*
    context.become
      Food(veg) -> stack.push(sadReceive)
      Food(chocolate) -> stack.push(happyReceive)
      Stack:
      1. happyReceive (tos)
      2. sadReceive
      3. happyReceive
     */

  /*
    new behavior, if context.become(sadState, false) when Food(Veg) && context.unbecome() when Food(happy), in sadState Receiver
    first incomming message = Food(veg)
    second incomming message = Food(veg)
    third incomming message = Food(choco)
    Stack:
    1. sadReceive
    2. sadReceive
    3.happyReceive (default)
   */

}

object ImprovedActorPart3StackDiscardOld extends App {

  object FussyKid {

    case object KidAccept

    case object KidReject

    val HAPPY = "happy"
    val SAD = "sad"
  }

  object Mom {

    case class MomStart(kidRef: ActorRef)

    case class Food(food: String)

    case class Ask(message: String)

    val VEGETABLE = "veggies"
    val CHOCOLATE = "chocolate"
  }

  class Mom extends Actor {

    import FussyKid._
    import Mom._

    override def receive: Receive = {
      case MomStart(kidRef) => {
        kidRef ! Food(VEGETABLE)
        kidRef ! Food(VEGETABLE)
        kidRef ! Food(CHOCOLATE)
        kidRef ! Ask("Do you want to play!")
      }
      case KidAccept        => println("Yay, my kid is happy!")
      case KidReject        => println("My kid is sad but at least he is happy!")
    }
  }

  class StatelessFussyKid extends Actor {

    import FussyKid._
    import Mom._

    override def receive: Receive = happyReceive

    def happyReceive: Receive = {
      case Food(VEGETABLE) => context.become(sadReceive, false) // happyHandler will be there in stack
      case Food(CHOCOLATE) =>
      case Ask(_)          => sender() ! KidAccept
    }

    def sadReceive: Receive = {
      case Food(VEGETABLE) => context.become(sadReceive, true) // push a sad handler on top of sad handler, discard oldSadHandler
      case Food(CHOCOLATE) => context.unbecome() // pop current handler from stack
      case Ask(_)          => sender() ! KidReject
    }
  }

  val system = ActorSystem("ChangingBehaviourDemo")
  val momRef = system.actorOf(Props[Mom])
  val kidRef = system.actorOf(Props[StatelessFussyKid])

  import Mom._

  momRef ! MomStart(kidRef)


  /*
    first incomming message = Food(veg), change state to sadHandler from happyHandler with discard = false, so happyReceiver will be in stack.
    second incomming message = Food(veg), change state to itself, discard = true, pops current sadReceive, adds new sadReceive
     Stack:
    1. sadReceive
    2.happyReceive (default)
    third incomming message = Food(choco), currently in sad state, context.unbecome(), pops sadReceive so
    Stack:
    1.happyReceive (default)
   */

}


/** *
 * So for state change, akka always checks and uses state on top os stack
 * Stateful to Stateless actor, mutable vars changes into receive block paramters mostly
 */

object StateLessIncDecActor extends App {

  // DOMAIN of the counter
  object Counter {

    case object Increment

    case object Decrement

    case object Print

  }

  class Counter extends Actor {

    import Counter._

    override def receive: Receive = countReceive(0)

    def countReceive(currentCount: Int): Receive = {
      case Increment => {
        println(s"[countReceive($currentCount)] incrementing")
        context.become(behavior = countReceive(currentCount + 1), discardOld = true)
      }
      case Decrement => {
        println(s"[countReceive($currentCount)] decrementing")
        context.become(countReceive(currentCount - 1), true)
      }
      case Print     => println(s"[countReceive($currentCount)] my current count is $currentCount")
    }
  }

  import Counter._

  val system = ActorSystem("Counter_Exercise")

  val counter = system.actorOf(Props[Counter], "myCounter")


  (1 to 20).foreach(_ => counter ! Increment)
  (1 to 20).foreach(_ => counter ! Decrement)
  counter ! Print
}

object VoterAggregatorStateFul extends App {

  case class Vote(candidate: String)

  case object VoterStatusRequest

  case class VoterStatusReply(candidate: Option[String])

  class Citizen extends Actor {
    var candidate: Option[String] = None

    override def receive: Receive = {
      case Vote(c)            => candidate = Some(c)
      case VoterStatusRequest => sender() ! VoterStatusReply(candidate)
    }
  }

  case class AggregateVotes(citizens: Set[ActorRef])

  class VoteAggregator extends Actor {
    var stillWaiting: Set[ActorRef] = Set()
    var currentStats: Map[String, Int] = Map()

    override def receive: Receive = {
      case AggregateVotes(citizens) => {
        stillWaiting = citizens
        citizens.foreach(x => x ! VoterStatusRequest)
      }
      //a citizen hasn't voted yet
      case VoterStatusReply(None) => {
        sender() ! VoterStatusRequest // this may lead to infinite loop
      }

      case VoterStatusReply(Some(candidate)) => {
        val newStillWaiting = stillWaiting - sender()
        val currentVotesOfCandidate = currentStats.getOrElse(candidate, 0)
        currentStats = currentStats + (candidate -> (currentVotesOfCandidate + 1))
        if (newStillWaiting.isEmpty) {
          println(s"[aggregator] poll stats: $currentStats")
        } else {
          stillWaiting = newStillWaiting
        }
      }

    }
  }

  val system = ActorSystem("VotesStatefulDemo")

  val alice = system.actorOf(Props[Citizen])
  val bob = system.actorOf(Props[Citizen])
  val charlie = system.actorOf(Props[Citizen])
  val daniel = system.actorOf(Props[Citizen])

  alice ! Vote("Martin")
  bob ! Vote("Jonas")
  charlie ! Vote("Roland")
  daniel ! Vote("Roland")

  val voteAggregator = system.actorOf(Props[VoteAggregator])
  voteAggregator ! AggregateVotes(Set(alice, bob, charlie, daniel))


}

object VoterAggregatorStateLess extends App {

  case class Vote(candidate: String)

  case object VoterStatusRequest

  case class VoterStatusReply(candidate: Option[String])

  class Citizen extends Actor {

    override def receive: Receive = {
      case Vote(c) => context.become(voted(c))
      case VoterStatusRequest => sender() ! VoterStatusReply(None)
    }

    def voted(candidate: String):Receive = {
      case VoterStatusRequest => sender() ! VoterStatusReply(Some(candidate))
    }
  }

  case class AggregateVotes(citizens: Set[ActorRef])

  class VoteAggregator extends Actor {

    override def receive: Receive = awaitingCommand

    def awaitingCommand: Receive = {
      case AggregateVotes(citizens) => {
        citizens.foreach(citizenRef => citizenRef ! VoterStatusRequest)
        context.become(awaitingStatuses(citizens, Map()))
      }
    }

    def awaitingStatuses(stillWaiting: Set[ActorRef], currentStats: Map[String, Int]): Receive = {
      case VoterStatusReply(None) => sender() ! VoterStatusRequest
      case VoterStatusReply(Some(candidate)) => {
        val newStillWaiting = stillWaiting - sender()
        val currentVotesOfCandidate = currentStats.getOrElse(candidate, 0)
        val newStats = currentStats + (candidate -> (currentVotesOfCandidate + 1))
        if (newStillWaiting.isEmpty) {
          println(s"[aggregator] poll stats: $newStats")
        } else {
          context.become(awaitingStatuses(newStillWaiting, newStats))
        }
      }
    }
  }

  val system = ActorSystem("VotesStatefulDemo")

  val alice = system.actorOf(Props[Citizen])
  val bob = system.actorOf(Props[Citizen])
  val charlie = system.actorOf(Props[Citizen])
  val daniel = system.actorOf(Props[Citizen])

  alice ! Vote("Martin")
  bob ! Vote("Jonas")
  charlie ! Vote("Roland")
  daniel ! Vote("Roland")

  val voteAggregator = system.actorOf(Props[VoteAggregator])
  voteAggregator ! AggregateVotes(Set(alice, bob, charlie, daniel))


}

