package com.aamir.akka.actors.infra


import akka.actor.{Actor, ActorLogging, ActorSystem, PoisonPill, Props}
import akka.dispatch.{ControlMessage, PriorityGenerator, UnboundedControlAwareMailbox, UnboundedPriorityMailbox}
import com.typesafe.config.{Config, ConfigFactory}

object Mailboxes1 extends App {

  /**
   * Interesting case #1 - custom priority mailbox
   * P0 -> most important
   * P1
   * P2
   * P3
   */

  class SimpleActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case message => log.info(message.toString)
    }
  }

  // step 1 - mailbox definition
  class SupportTicketPriorityMailbox(settings: ActorSystem.Settings, config: Config)
    extends UnboundedPriorityMailbox(
      PriorityGenerator {
        case message: String if message.startsWith("[P0]") => 0
        case message: String if message.startsWith("[P1]") => 1
        case message: String if message.startsWith("[P2]") => 2
        case message: String if message.startsWith("[P3]") => 3
        case _                                             => 4
      }
    )


  val system = ActorSystem("MailboxDemo", ConfigFactory.load().getConfig("mailboxesDemo"))
  // step 2 - make it known in the config
  // step 3 - attach the dispatcher to an actor

  val supportTicketLogger = system.actorOf(Props[SimpleActor].withDispatcher("support-ticket-dispatcher"))
  //supportTicketLogger ! PoisonPill
  //Thread.sleep(1000)
  supportTicketLogger ! "[P3] this thing would be nice to have"
  supportTicketLogger ! "[P0] this needs to be solved NOW!"
  supportTicketLogger ! "[P1] do this when you have the time"
  supportTicketLogger ! "a normal message"
  supportTicketLogger ! "second normal message"

}

object Mailboxes2 extends App {
  // after which time can I send another message and be prioritized accordingly?

  /**
   * Interesting case #2 - control-aware mailbox
   * we'll use UnboundedControlAwareMailbox
   */

  class SimpleActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case message => log.info(message.toString)
    }
  }


  // step 1 - mark important messages as control messages
  case object ManagementTicket extends ControlMessage
  val system = ActorSystem("MailboxDemo", ConfigFactory.load().getConfig("mailboxesDemo"))

  val controlAwareActor = system.actorOf(Props[SimpleActor].withMailbox("control-mailbox"))
    controlAwareActor ! "[P0] this needs to be solved NOW!"
    controlAwareActor ! "[P1] do this when you have the time"
    controlAwareActor ! ManagementTicket


}

object Mailboxes3 extends App {

  // method #2 - using deployment config
  class SimpleActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case message => log.info(message.toString)
    }
  }


  case object ManagementTicket extends ControlMessage
  val system = ActorSystem("MailboxDemo", ConfigFactory.load().getConfig("mailboxesDemo"))

  val altControlAwareActor = system.actorOf(Props[SimpleActor], "altControlAwareActor")
  altControlAwareActor ! "[P0] this needs to be solved NOW!"
  altControlAwareActor ! "[P1] do this when you have the time"
  altControlAwareActor ! ManagementTicket


}