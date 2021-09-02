package devops.code

import org.apache.kafka.clients.producer.ProducerConfig._
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.{IntegerSerializer, StringSerializer}

import java.util.Properties

//Todo: write a consumer that subscribes to multiple topics
//Todo: Question: Do we have to commit ourselves in consumers, otherwise while restarting it will fetch all records
object ProducerPlayground extends App {

  def customPartitioner(key: Int):Int = key % 3

  val topicName = "test-1"
  val producerProperties = new Properties()
  producerProperties.setProperty(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  producerProperties.setProperty(KEY_SERIALIZER_CLASS_CONFIG, classOf[IntegerSerializer].getName)
  producerProperties.setProperty(VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)

  val producer = new KafkaProducer[Int, String](producerProperties)

  producer.send(new ProducerRecord[Int, String](topicName,0, 10,"Message 1"))
  producer.send(new ProducerRecord[Int, String](topicName,0, 20,"Message 2"))
  producer.send(new ProducerRecord[Int, String](topicName,1, 30, "Message 3"))
  producer.send(new ProducerRecord[Int, String](topicName, 1,40,"Message 4"))
  producer.send(new ProducerRecord[Int, String](topicName, 2,50,"Message 5"))
  producer.send(new ProducerRecord[Int, String](topicName,2,60, "Message 6"))

  producer.flush()

}

