package com.aamir.json4s


import com.aamir.json4s.MakeJsonTest.s
import com.aamir.json4s.TwoDArray1.s1
import json.BaseJsonUtilities
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

object FileJArrayTest extends App {


  def filterJArrayFields(jValue: JValue, excludedField: String): Boolean = {
    val jObject = jValue.asInstanceOf[JObject]
    val optionalField = jObject.values.get(excludedField)
    if(optionalField.isDefined) optionalField.get == "true" || optionalField.get == true else false
  }
  val jsonResponse = """{"fileMetadatas":[{"fileName":"Form-A Template2.0.pdfsss","fileSourceId":"http://s3.us-east-1.amazonaws.com/azure-production/07c47d90-5e82-41d0-aedb-8fcfd1d9e0e1/Form-A+Template2.0.pdf","identified":true,"mimeType":"application/pdf","templateType":"Type-29"},{"fileName":"Form-A Template2.0.pdfaaa","fileSourceId":"http://s3.us-east-1.amazonaws.com/azure-production/07c47d90-5e82-41d0-aedb-8fcfd1d9e0e1/Form-A+Template2.0.pdf","identified":true,"mimeType":"application/pdf","templateType":"Type-29"},{"fileName":"Form-A Template2.0.pdf kkbg","fileSourceId":"http://s3.us-east-1.amazonaws.com/azure-production/07c47d90-5e82-41d0-aedb-8fcfd1d9e0e1/Form-A+Template2.0.pdf","identified":false,"mimeType":"application/pdf","templateType":"Type-29"}],"message":"File type identified successfully","solutionName":"cvcm","solutionProcessId":"5fbba49601ee3c00122ade6f","statusCode":200}""".stripMargin

  val messageJObject = parse(jsonResponse).asInstanceOf[JObject]
  val fileMetaData   = (messageJObject \ "fileMetadatas").asInstanceOf[JArray]
  val res = fileMetaData.arr.partition(x => filterJArrayFields(x, "identified"))

  println(compact(JArray(res._1)))
  println(compact(JArray(res._2)))

}

object TwoDArray1 extends App {

  // val s = """[{"name":"메뉴1","permission":"1","link":"http://naver.com"},{"name":"메뉴2","permission":"2","link":"http://daum.net","sub":[{"name":"메뉴2-1","permission":"1","link":"http://google.com"},{"name":"메뉴2-2","permission":"1","link":"http://yahoo.com"}]}]""".stripMargin

  val s1 = s"""{"key" : "value"}"""
  val s2 = s"""[{"key" : "value"}, {"key" : "value"}]"""
//  println(parse(s2).asInstanceOf[JObject])

  val r1 = parse(s1) match {
    case x: JArray => true
    case _ => false
  }
  println(r1)
  println(parse(s1).isInstanceOf[JObject])

  println("---"* 10)

  println(parse(s2).isInstanceOf[JArray])
  println(parse(s2).isInstanceOf[JObject])

  val ss = s"""[{"key" : "value"}, {"key" : "value"}]"""
  val ff = parse(s2).asInstanceOf[JArray]
  val xx = JObject("nerData" -> ff)
 println(compact(xx))
}

object TwoDArray2 extends App {

  // val s = """[{"name":"메뉴1","permission":"1","link":"http://naver.com"},{"name":"메뉴2","permission":"2","link":"http://daum.net","sub":[{"name":"메뉴2-1","permission":"1","link":"http://google.com"},{"name":"메뉴2-2","permission":"1","link":"http://yahoo.com"}]}]""".stripMargin

  val s1 = s"""{"narrative" : "value"}"""
  val r1 = parse(s1)

   val res1 = r1.asInstanceOf[JObject]
   val res:JValue = res1 \ "narrative"

  val r1d = res match {
    case x: JArray => true
    case _ => false
  }

  println(r1d)


}

object ConfumdrTest extends App {
  case class ConsumerConfig(consumerName: String,
                            mqConfiguration: MQConfig,
                            workerConfig: List[WorkerConfig],
                            serviceUrls: Map[String, String],
                            batchSize: Option[Int] = Some(1),
                            createdBy: String,
                            createdTime: String,
                            updatedBy: Option[String],
                            updatedTime: Option[String],
                            status: String = "active")

  case class MQConfig(mqType: String,
                      serverUrls: List[String],
                      userName: Option[String],
                      password: Option[String],
                      consumerGroupId: String,
                      consumerGroupInstanceId: Option[String] = None,
                      topicName: String,
                      replica: Int = 1) {

  }

  case class WorkerConfig(workerName: String,
                          workerType: String,
                          responseType: String,
                          packageName: Option[String] = None){

  }
    val wc = WorkerConfig("w1", "wt", "dd", None)
    val mq = MQConfig("", Nil, None, None, "dd", None, "edde", 10)

    val cc = ConsumerConfig("myconsumer",mq,List(wc),Map.empty, Some(11), "dd","dd",None,None,"dd")

  import BaseJsonUtilities._

  //val json = cc.asJson
  //println(json)

  val ccJson = """{"consumerName":"myconsumer","mqConfiguration":{"mqType":"","serverUrls":[],"userName":null,"password":null,"consumerGroupId":"","consumerGroupInstanceId":null,"topicName":"edde","replica":10},"workerConfig":[{"workerName":"w1","workerType":"wt","responseType":"dd","packageName":null}],"serviceUrls":{},"batchSize":11,"createdBy":"dd","createdTime":"dd","updatedBy":null,"updatedTime":null,"status":"dd"}""".stripMargin

  import json.BaseJsonUtilities._
   extractEntityWithTry[ConsumerConfig](ccJson) match {
    case scala.util.Success(s) =>
      println(s)
    case scala.util.Failure(e) =>
      println(e.getMessage)
  }

}

object Array2DTest extends App {

  val oldJson = JObject("someKey" -> JString("someValue"))

  val s = """{"nonNarrative":[[{"name":"메뉴2-1","permission":"1","link":"http://google.com"},{"name":"메뉴2-2","permission":"1","link":"http://yahoo.com"}],[{"name":"메뉴2-1","permission":"1","link":"http://google.com"},{"name":"메뉴2-2","permission":"1","link":"http://yahoo.com"}]]}""".stripMargin

  val j = parse(s).asInstanceOf[JObject]

  val arr = (j \ "nonNarrative").asInstanceOf[JArray]

  arr.arr.foreach { arrayItem =>
    println(compact(oldJson))
    val res = oldJson.asInstanceOf[JValue] merge JObject("nonNarrative" -> arrayItem)
    println(compact(res))
    println("--" * 10)
  }

}

object EmptyArrayAddTest extends App {

  // val s = """[{"name":"메뉴1","permission":"1","link":"http://naver.com"},{"name":"메뉴2","permission":"2","link":"http://daum.net","sub":[{"name":"메뉴2-1","permission":"1","link":"http://google.com"},{"name":"메뉴2-2","permission":"1","link":"http://yahoo.com"}]}]""".stripMargin

  val s1 = s"""{"key" : "value"}"""

  val jObj = parse(s1).asInstanceOf[JObject]

  val jArray = JObject("abc" -> JArray(Nil))


  val res = jObj merge jArray merge JObject("def" -> JObject())
  println(compact(res))
}