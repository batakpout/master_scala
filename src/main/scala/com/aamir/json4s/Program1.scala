package com.aamir.json4s


import com.aamir.json4s.MakeJsonTest.s
import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods._
import org.json4s.{JArray, JField, JNothing, JObject, JString}
import play.api.libs.json.{JsObject, JsValue}

import scala.util.Try

object MessageParsers {

  def getAcknowledgement(message: String): Try[String] = {
    Try {
      val jsObject = parse(message).asInstanceOf[JObject]
      val ack = jsObject \ "ack"
      ack.values.toString
    }
  }
}

object MakeJsonTest extends App {

  def removeJArrayFields(jValue: JValue, excludedFields: List[String]): Boolean = {
    val jObject = jValue.asInstanceOf[JObject]
    jObject.values.keySet.exists(excludedFields.contains(_))
  }

  val json = s"""{"ack" : "12-09k-9", "temp1" : "ll", "kkbg" : "dade" , "fileMetadatas" : [{"key1":"value1"}, {"key2":"value2"}, {"key3":"value3"}]}"""
  val jsObject = parse(json).asInstanceOf[JObject]

  val ack = jsObject \ "ack"
  val temp1 = jsObject \ "temp1"

  val fileMetaData = (jsObject \ "fileMetadatas").asInstanceOf[JArray]
  val updatedFileMetaData = JArray(fileMetaData.arr.filterNot(x => removeJArrayFields(x, List("kkbg"))))
  val requestJson = JObject("fileMetadatas" -> updatedFileMetaData, "clientId" -> ack, "sluciotnId" -> temp1)
  val s = compact(requestJson)
  println(s)
}

object Test extends App {

  def removeFields(jsObject: JValue, fields: List[String]): JValue = {
    def rec(jsObject: JValue, fields: List[JValue]): JValue = fields match {
      case Nil    => jsObject
      case h :: t => rec(jsObject.remove(_ == h), t)
    }

    val res = fields map { field =>
      jsObject \ field
    }

    rec(jsObject, res)

  }

  val json = s"""{"ack" : "12-09k-9", "temp1" : "ll", "fileMetadatas" : [{"key1":"value1"}, {"key2":"value2"}, {"key3":"value3"}]}"""
  val jsObject = parse(json).asInstanceOf[JObject]

  val rList = List("ack", "temp1")
  println(jsObject)
  val res = removeFields(jsObject, rList)
  println("********")
  println(res)


  val fileMetaData = (jsObject \ "fileMetadatas").asInstanceOf[JArray]

  //println(fileMetaData.arr)
  //val removeKeylist = List("key1", "key3")
  val jValue = JObject("ack" -> JString("anything"))

  val l = List("key1", "key2")

  def f(jValue: JValue) = {
    val jObject = jValue.asInstanceOf[JObject]
    jObject.values.keySet.exists(l.contains(_))
  }

  val jsonArrayKeys = fileMetaData.arr.filterNot(f)


  println(jsonArrayKeys)
  //

  //println(jValue.values.keys)

  /* val x = fileMetaData.arr.filterNot(x => ll.contains(x.))
   println(x)*/


}

object PartitionTest extends App {

  def filterJArrayFields(jValue: JValue, excludedField: String): Boolean = {
    val jObject = jValue.asInstanceOf[JObject]
    val x = jObject.values.get(excludedField)
    if (x.isDefined) x.get == "true" else false

  }

  val json = s"""{"fileMetadatas" : [{"identified":"false"}, {"identified":"true"}, {"identified":"true"}, {"identified":"true"}]}"""
  val jsObject = parse(json).asInstanceOf[JObject]
  val jArray = (jsObject \ "fileMetadatas").asInstanceOf[JArray]

  println(jArray)
  val (ident, uniden) = jArray.arr.partition(x => filterJArrayFields(x, "identified"))

  println(ident)
  println(uniden)

}

object AddSameItemTest extends App {

  def filterJArrayFields(jValue: JValue, excludedField: String): Boolean = {
    val jObject = jValue.asInstanceOf[JObject]
    val x = jObject.values.get(excludedField)
    if (x.isDefined) x.get == "true" else false

  }

  val json = s"""{ "acl" : "se0ee-899", "fileMetadatas" : [{"identified":"false"}, {"identified":"true"}, {"identified":"true"}, {"identified":"true"}]}"""
  val jsObject = parse(json).asInstanceOf[JObject]
  val jArray = (jsObject \ "fileMetadatas").asInstanceOf[JArray]


  println(compact(jArray))
  val (ident, uniden) = jArray.arr.partition(x => filterJArrayFields(x, "identified"))

  println(ident)
  println(compact(JArray(uniden)))

  //JObject("fileMedatas" -> JArray(ident))
  val res = jsObject.transform {
    case obj: JObject => obj.findField(_.equals(JField("fileMetadatas", jArray))) match {
      case None    => obj
      case Some(x) => obj.transformField { case JField(k, v) if k == "fileMetadatas" => JField(k, JArray(uniden)) }
    }
  }

  val x1 = JObject("foo" -> JString("bar"))
  val x3 = JObject("goo" -> JString("lar"))
  val finalRes = res merge x1 merge x3 merge jsObject
  println(compact(finalRes))
  val finalResx = finalRes merge JArray(uniden)
  println(compact(finalResx))

}

object NotPresentFieldsTest extends App {

  val json = s"""{"ack" : "12-09k-9", "temp1" : "ll", "kkbg" : "dade" , "fileMetadatas" : [{"key1":"value1"}, {"key2":"value2"}, {"key3":"value3"}]}"""
  val jsObject = parse(json).asInstanceOf[JObject]

  val res = (jsObject \ "ack").toSome.map(_.values.toString).getOrElse("")
  println(res)
}