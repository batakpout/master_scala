package akka.actors.part1

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}

/**
 *         log.info(s"info message 1: $message")
 *         log.debug(s"debug message 2: $message")
 *         log.error(s"error message 3: $message")
 *         log.warning(s"warning message 4: $message")
 *
 * when loglevel = "DEBUG" then all logs will come in console
 * when loglevel = "WARNING" only warn, error message will come in console
 * when loglevel = "ERROR" only error message will come in console
 * when loglevel = "INFO" then info, warning and error log will come in console
 * when application.conf file doesn't exit then by default it takes loglevel=INFO
 *
 */
object ActorConf1 extends App {
  /**
   * 1. Inline-Configuration
   */
  val configString =
    """
      |akka {
      | loglevel = "INFO"
      | }
      |""".stripMargin
  val config: Config = ConfigFactory.parseString(configString)
  val system = ActorSystem("ActorConfig1Demo", config)
  val actor = system.actorOf(Props(new Actor with ActorLogging {
    override def receive: Receive = {
      case message: String => {
        log.info(s"info message 1: $message")
        log.debug(s"debug message 2: $message")
        log.error(s"error message 3: $message")
        log.warning(s"warning message 4: $message")
      }
    }
  }))
  actor ! "tommahank"
}

/**
 * 2. Using application.conf file
 */
object ActorConfig2 extends App {
  class SimpleLoggingActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case message: String => {
        log.info(s"info message 1: $message")
        log.debug(s"debug message 2: $message")
        log.error(s"error message 3: $message")
        log.warning(s"warning message 4: $message")
      }
    }
  }

  val system = ActorSystem("DefaultConfigFileDemo")
  val actor = system.actorOf(Props[SimpleLoggingActor])
  actor ! "Remember me"
}

/**
 * 3. separate configuration in same application.conf file
 */
object ActorConfig3 extends App {
  class SimpleLoggingActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case message: String => {
        log.info(s"info message 1: $message")
        log.debug(s"debug message 2: $message")
        log.error(s"error message 3: $message")
        log.warning(s"warning message 4: $message")
      }
    }
  }

  val specialConfig: Config = ConfigFactory.load().getConfig("mySpecialConfig")
  val system = ActorSystem("DefaultConfigFileDemo", specialConfig)
  val actor = system.actorOf(Props[SimpleLoggingActor])
  actor ! "Remember me"
}

/**
 * 4 - separate config in another file
 */

object ActorConfig4 extends App {
  class SimpleLoggingActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case message: String => {
        log.info(s"info message 1: $message")
        log.debug(s"debug message 2: $message")
        log.error(s"error message 3: $message")
        log.warning(s"warning message 4: $message")
      }
    }
  }

  val separateConfig: Config = ConfigFactory.load("secretfolder/secretconfiguration.conf")

  println(s"separate config log level: ${separateConfig.getString("akka.loglevel")}")

  val system = ActorSystem("DefaultConfigFileDemo", separateConfig)
  val actor = system.actorOf(Props[SimpleLoggingActor])
  actor ! "Remember me"
}

/**
 * 5 - different file formats
 * JSON
 */

object ActorConfig5 extends App {
  class SimpleLoggingActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case message: String => {
        log.info(s"info message 1: $message")
        log.debug(s"debug message 2: $message")
        log.error(s"error message 3: $message")
        log.warning(s"warning message 4: $message")
      }
    }
  }

  val jsonConfig: Config = ConfigFactory.load("json/jsonConfig.json")
  println(s"json config: ${jsonConfig.getString("aJsonProperty")}")
  println(s"json config: ${jsonConfig.getString("akka.loglevel")}")

  val system = ActorSystem("DefaultConfigFileDemo", jsonConfig)
  val actor = system.actorOf(Props[SimpleLoggingActor])
  actor ! "Remember me"
}

/**
 * 6 - different file formats
 *  Properties
 */

object ActorConfig6 extends App {
  class SimpleLoggingActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case message: String => {
        log.info(s"message 1 : $message")
        log.warning(s"message 2 : $message")
      }
    }
  }

  val propsConfig: Config = ConfigFactory.load("props/propsConfig.properties")
  println(s"json config: ${propsConfig.getString("my.simpleProperty")}")
  println(s"json config: ${propsConfig.getString("akka.loglevel")}")

  val system = ActorSystem("DefaultConfigFileDemo", propsConfig)
  val actor = system.actorOf(Props[SimpleLoggingActor])
  actor ! "Remember me"
}