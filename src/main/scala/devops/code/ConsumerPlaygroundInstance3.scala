package devops.code

import org.apache.kafka.clients.consumer.ConsumerConfig._
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.{StringDeserializer, IntegerDeserializer}

import java.time.Duration
import java.util.Properties
import scala.jdk.CollectionConverters._
object ConsumerPlaygroundInstance3 extends App {

  val topicName = "test-1"
  val consumerProperties = new Properties()
  consumerProperties.setProperty(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  consumerProperties.setProperty(GROUP_ID_CONFIG, "group-id-1")
  consumerProperties.setProperty(AUTO_OFFSET_RESET_CONFIG, "latest") //"earliest" "latest"
  consumerProperties.setProperty(KEY_DESERIALIZER_CLASS_CONFIG, classOf[IntegerDeserializer].getName)
  consumerProperties.setProperty(VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
  consumerProperties.setProperty(ENABLE_AUTO_COMMIT_CONFIG, "false")

  val consumer = new KafkaConsumer[Int, String](consumerProperties)
  consumer.subscribe(List(topicName).asJava)

  println("| key | Message | Partition | Offset |")
  while (true) {
    val polledRecord = consumer.poll(Duration.ofSeconds(2)).asScala
    println(s"Polled ${polledRecord.size} records")
    for(record <- polledRecord.iterator) {
      println(s"| ${record.key()} | ${record.value()} | ${record.partition()} | ${record.offset()} |")
    }
    consumer.commitSync(Duration.ofSeconds(1))
    Thread.sleep(3000)
  }

}