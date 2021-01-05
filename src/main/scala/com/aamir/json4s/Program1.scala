package com.aamir.json4s


import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods._
import org.json4s.{JArray, JField, JObject, JString}
import play.api.libs.json.{JsObject, JsValue}

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

  def removeFields(jsObject: JValue, fields: List[JValue]):JValue = fields match {
    case Nil => jsObject
    case h :: t => removeFields(jsObject.remove(_ == h), t)
  }

  val json = s"""{"ack" : "12-09k-9", "temp1" : "ll", "fileMetadatas" : [{"key1":"value1"}, {"key2":"value2"}, {"key3":"value3"}]}"""
  val jsObject = parse(json).asInstanceOf[JObject]

  val ack = jsObject \ "ack"
  val temp1 = jsObject \ "temp1"
  //println(jsObject)
  val res = removeFields(jsObject, List(ack, temp1))
 // println(res)





  val fileMetaData = (jsObject \ "fileMetadatas").asInstanceOf[JArray]

  //println(fileMetaData.arr)
  //val removeKeylist = List("key1", "key3")
  val jValue = JObject("ack" -> JString("anything"))

  val l = List("key1", "key2")

  def f(jValue: JValue) = {
    val jObject = jValue.asInstanceOf[JObject]
    jObject.values.keySet.exists(l.contains(_))
  }

  val jsonArrayKeys =  fileMetaData.arr.filterNot(f)

  println(jsonArrayKeys)
 //

  //println(jValue.values.keys)

 /* val x = fileMetaData.arr.filterNot(x => ll.contains(x.))
  println(x)*/



}