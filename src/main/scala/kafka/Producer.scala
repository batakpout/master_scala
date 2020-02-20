package kafka

import java.util.Properties
import org.apache.kafka.clients.producer._
object Producer1 {
  def main(args: Array[String]): Unit = {
    writeToKafka("quick-start1")
    println("====done====")
  }
  def writeToKafka(topic: String): Unit = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)
    val record = new ProducerRecord[String, String](topic, "key", "one more message from producer...")
    producer.send(record)
    producer.close()
  }
}

object Producer2 {
  def main(args: Array[String]): Unit = {
    writeToKafka("quick-start2")
    println("====done====")
  }
  def writeToKafka(topic: String): Unit = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)
    val record = new ProducerRecord[String, String](topic, "key", "one more message from producer 2 ...")
    producer.send(record)
    producer.close()
  }
}

object Producer3 {
  def main(args: Array[String]): Unit = {
    writeToKafka("quick-start3")
    println("====done====")
  }
  def writeToKafka(topic: String): Unit = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)
    val record = new ProducerRecord[String, String](topic, "key", "one more message from producer3...")
    producer.send(record)
    producer.close()
  }
}

