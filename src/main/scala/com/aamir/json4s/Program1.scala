package com.aamir.json4s


import org.json4s.jackson.JsonMethods._
import org.json4s.{JArray, JObject}

import scala.util.Try

object MessageParsers {

  def getAcknowledgement(message: String):Try[String] = {
    Try{
      val jsObject = parse(message).asInstanceOf[JObject]
      val ack = jsObject \ "ack"
      ack.values.toString
    }
  }
}

object Test extends App {

  val json = s"""{"ack" : "12-09k-9", "fileMetadatas" : [{"key1":"value1"}, {"key2":"value2"}]}"""
  val jsObject = parse(json).asInstanceOf[JObject]
  val ack = jsObject \ "ack"


  val fileMetaData = (jsObject \ "fileMetadatas").asInstanceOf[JArray]
  println(jsObject)

  val newJsObject = jsObject.remove(_ == ack)
  println(newJsObject)

  println {
    ack.values.toString
  }

  println(fileMetaData.arr.isEmpty)

}