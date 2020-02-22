package kafka

import java.util
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Properties
import scala.collection.JavaConverters._

object Consumer {
  def main(args: Array[String]): Unit = {
    consumeFromKafka(List("quick-start1","quick-start2","quick-start3"))
  }

  def consumeFromKafka(topics: List[String]) = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("auto.offset.reset", "latest")
    props.put("group.id", "consumer-group")
    val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
    consumer.subscribe(topics.asJava)
    while (true) {
      val record = consumer.poll(1000).asScala
      for (data <- record.iterator) {
        println("Topic: " + data.topic() +
          ",Key: " + data.key() +
          ",Value: " + data.value() +
          ", Offset: " + data.offset() +
          ", Partition: " + data.partition())
        println("==========================================")
      }
    }
  }
}