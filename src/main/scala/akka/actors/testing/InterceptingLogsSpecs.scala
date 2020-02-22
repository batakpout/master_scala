package akka.actors.testing

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.testkit.{EventFilter, ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

/**
 * Using EventFilters to intercept logs, intercepting exceptions.
 * Event filtering is good for:-
 * 1. integration tests where its hard to do message based testing.
 * 2. there are logs to inspect.
 * */

class InterceptingLogsSpecs extends TestKit(ActorSystem("InterceptingLogsSpecs", ConfigFactory.load().getConfig("interceptingLogMessages")))
  with ImplicitSender with WordSpecLike with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  import InterceptingLogsSpecs._
  val item = "Rock the JVM Akka course"
  val creditCard = "1234-1234-1234-1234"
  val invalidCreditCard = "0000-0000-0000-0000"

  "A CheckoutFlow" should {
    "correctly log the dispatch of an order" in {
      //occurrences = 1 means catches log one time only
      EventFilter.info(pattern = s"order with [0-9]+ for item $item has been dispatched...", occurrences = 1) intercept {
        val checkoutActor = system.actorOf(Props[CheckoutActor])
        checkoutActor ! Checkout(item, creditCard)
      }
    }

    "freak out if the payment is denied" in {
      EventFilter[RuntimeException](occurrences = 1) intercept {
        val checkoutActor = system.actorOf(Props[CheckoutActor])
        checkoutActor ! Checkout(item, invalidCreditCard)
      }
    }
  }
}

object InterceptingLogsSpecs {

  case class Checkout(item: String, creditCard: String)

  case class AuthorizeCard(creditCard: String)

  case class DispatchItem(item: String)

  case object PaymentAccepted

  case object PaymentDenied

  case object OrderConfirmed

  class CheckoutActor extends Actor {

    private val paymentManagerActor = context.actorOf(Props[PaymentManager])
    private val fulfillmentActor = context.actorOf(Props[FulfillmentManager])

    override def receive: Receive = awaitingCheckout

    def awaitingCheckout: Receive = {
      case Checkout(item, card) => {
        paymentManagerActor ! AuthorizeCard(card)
        context.become(pendingPayment(item))
      }
    }

    def pendingPayment(item: String): Receive = {
      case PaymentAccepted => {
        fulfillmentActor ! DispatchItem(item)
        context.become(pendingFulfillment())
      }
      case PaymentDenied => {
        throw new RuntimeException("I can't handle this anymore!")
      }
    }

    def pendingFulfillment(): Receive = {
      case OrderConfirmed => context.become(awaitingCheckout)
    }
  }

  class PaymentManager extends Actor {
    override def receive: Receive = {
      case AuthorizeCard(card) => {
        if (card.startsWith("0")) sender() ! PaymentDenied else {
          Thread.sleep(5000)
          sender() ! PaymentAccepted
        }
      }
    }
  }

  class FulfillmentManager extends Actor with ActorLogging {
    var orderId = 42

    override def receive: Receive = {
      case DispatchItem(item) => {
        orderId += 1
        log.info(s"order with $orderId for item $item has been dispatched...")
        sender() ! OrderConfirmed
      }
    }
  }

}

