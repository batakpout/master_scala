package json


import org.json4s.Extraction
import org.json4s.JsonAST.JValue
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods.{compact, parse, render}

import scala.util.Try

import java.sql.Time
import java.text.SimpleDateFormat
import java.time.{LocalDate, LocalDateTime}
import java.util.UUID

import org.joda.time.{DateTime, LocalTime}
import org.joda.time.format.DateTimeFormat
import org.json4s.JsonAST.JNothing
import org.json4s.{DefaultFormats, FieldSerializer, Formats}

import scala.collection.mutable.ListBuffer
trait NameLike {
  val name: String
}
trait Serializer {

  import org.json4s.CustomSerializer

  val serializers: ListBuffer[CustomSerializer[_]] = ListBuffer()
  val appendedFieldSerializers: ListBuffer[FieldSerializer[_]] = ListBuffer()

  def appendCustomSerializers(newSerializers: List[CustomSerializer[_]]) = {
    //serializers.appendAll(newSerializers)
    newSerializers.foreach(x => serializers.append(x))
  }

  def appendCustomFieldSerializer(newFieldSerializers: List[FieldSerializer[_]]) = {
    //serializers.appendAll(newSerializers)
    newFieldSerializers.foreach(x => appendedFieldSerializers.append(x))
  }

  implicit lazy val serializerFormats: Formats = {
    val formats: Formats = new DefaultFormats {
      override def dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

      override val fieldSerializers: List[(Class[_], FieldSerializer[_])] = appendedFieldSerializers.map(s => s.mf.runtimeClass -> s).toList

      // override val emptyValueStrategy: EmptyValueStrategy = EmptyValueStrategy.preserve

    } ++ serializers.toList
    formats.preservingEmptyValues
  }//.preservingEmptyValues
}
trait BaseTypeSerializers extends Serializer {

  import org.json4s.CustomSerializer
  import org.json4s.JsonAST.{JNull, JString}

  val timeOnlyFormatter = DateTimeFormat.forPattern("HH:mm:ss")
  val dateOnlyFormatter = DateTimeFormat.forPattern("yyyy-MM-dd")
  val dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZoneUTC()

  case object SqlDateSerializer extends CustomSerializer[java.sql.Date](format => ( {
    case JString(date) => new java.sql.Date(dateOnlyFormatter.parseDateTime(date).getMillis)
    case JNull         => null
  }, {
    case date: java.sql.Date => JString(date.toString)
  }))

  case object SqlTimeSerializer extends CustomSerializer[java.sql.Time](format => ( {
    case JString(time) => new Time(timeOnlyFormatter.parseDateTime(time).getMillis)
    case JNull         => null
  }, {
    case time: java.sql.Time => JString(time.toString)
  }))


  case object LocalDateSerializer extends CustomSerializer[LocalDate](format => ( {
    case JString(date) => LocalDate.parse(date)
    case JNull         => null
  }, {
    case date: LocalDate => JString(date.toString)
  }
  ))

  case object JodaLocalDateSerializer extends CustomSerializer[org.joda.time.LocalDate](format => ( {
    case JString(date) => org.joda.time.LocalDate.parse(date)
    case JNull         => null
  }, {
    case date: org.joda.time.LocalDate => JString(date.toString)
  }
  ))

  case object JodaDateSerializer extends CustomSerializer[DateTime](format => ( {
    case JString(date) => DateTime.parse(date, dateTimeFormat)
    case JNull         => null
  }, {
    case date: DateTime => JString(date.toString(dateTimeFormat))
  }
  ))

  case object JodaTimeSerializer extends CustomSerializer[LocalTime](format => ( {
    case JString(time) => LocalTime.parse(time, timeOnlyFormatter)
    case JNull         => null
  }, {
    case time: LocalTime => JString(time.toString(timeOnlyFormatter))
  }
  ))

  case object UUIDSerializer extends CustomSerializer[UUID](format => ( {
    case JString(id) => UUID.fromString(id)
    case JNull       => null
  }, {
    case uuid: UUID => JString(uuid.toString)
  }
  ))

  //  case object NoneJNullSerializer extends CustomSerializer[Option[_]](format => ( {
  //    case JNull => None
  //    case JInt(id) => Some(id)
  //    case JString(str) => Some(str)
  //    case JDouble(d) => Some(d)
  //   // case JObject(v) => Some(JObjectParser)
  //    //case v:JValue => println("any => "+v); EmptyValueStrategy.preserve.replaceEmpty(v) //Some(any)
  //
  //  }, {
  //    case None => JNull
  //  }
  //  ))

  case object ManifestSerializer extends  CustomSerializer[Manifest[_]](formats => (
    PartialFunction.empty,
    { case _: Manifest[_] => JNothing }
  ))

  val nameListSerializer: FieldSerializer[NameLike] = FieldSerializer[NameLike]()

  appendCustomSerializers(List(SqlDateSerializer, SqlTimeSerializer, LocalDateSerializer, UUIDSerializer, JodaDateSerializer, JodaTimeSerializer, JodaLocalDateSerializer,ManifestSerializer))
  appendCustomFieldSerializer(List(nameListSerializer))
}
object LocalDateExtension {

  implicit class extension(s: LocalDateTime) {
    def between(start: LocalDateTime, end: LocalDateTime): Boolean = {
      s.isAfter(start) && s.isBefore(end)
    }
  }

}

trait RequestResponseEntity
abstract class JsUBEntity[E, TId](implicit val manifest1: Manifest[E]) {
  def attributes: Map[String, String] = Map.empty

  val createdBy: Long
}
trait Persisteble[TId] extends RequestResponseEntity {
  val id: TId
}
abstract class AggregateRoot[E, TId](implicit val mf: Manifest[E]) extends JsUBEntity[E, TId] with Persisteble[TId] {
  /*  def set(cc:Long ,createdDate:Timestamp,modifiedBy:Long,modifiedDate:Timestamp)(implicit session:Session)={
       createdBy = cc
    }*/
}
trait BaseJsonUtilitiesComponent  { this : BaseTypeSerializers =>
  import scala.language.implicitConversions

  implicit class CustomJson(jValue: JValue) {
    def append(key: String, c: Any): JValue = jValue.merge(render(key -> Extraction.decompose(c)))

    def appendJson(key: String, value: String) = jValue.merge(render(key -> parse(value)))

    def append(tuple: (String, Any)): JValue = jValue.merge(render(tuple._1 -> Extraction.decompose(tuple._2)))

    def append(key: String, c: Option[Any]): JValue = jValue.merge(render(key -> Extraction.decompose(c)))

    def append(key: String, c: List[Any]): JValue = jValue.merge(render(key -> Extraction.decompose(c)))

    def append(c: JValue): JValue = jValue.merge(c)

    def append(c: AnyRef): JValue = jValue.merge(Extraction.decompose(c))

    def appendList(key: String, list: List[JValue]) = jValue.merge(render(key -> Extraction.decompose(list)))

    def remove(key: String): JValue = jValue.removeField { x => x._1 == key}

    def remove(keys: List[String]): JValue = jValue.removeField { x => keys.contains(x._1)}

    def asJson = compact(jValue)
  }



  implicit def convertToJValue(x: Any) = new CustomJson(Extraction.decompose(x))

  implicit def convertToJValue2(x: Option[Any]) = new CustomJson(Extraction.decompose(x))

  // x.getOrElse(null)//new CustomJson(Extraction.decompose(x))
  implicit def convertToJValue3(x: (String, Option[Any])) = new CustomJson(render(x._1 -> Extraction.decompose(x._2.getOrElse(null))))

  implicit def convertToJValue4(x: (String, List[JValue])) = new CustomJson(render(x._1 -> x._2))

  implicit def convertToJValue1(d: (String, AggregateRoot[_,Long])) = new CustomJson(render(d._1 -> Extraction.decompose(d._2)))

  implicit def convertJValueToString(jValue: JValue): String = compact(jValue)

  implicit def convertJValueToString(jValueList: List[JValue]): String = compact(jValueList)

  def toJson(value: Any): String = {
    if (value.isInstanceOf[String]) value.asInstanceOf[String] else value.asJson
  }


  @deprecated("Use extractEntityWithTry instead", "Jul 13, 0.8.9.71-DEV-SNAPSHOT")
  def extractEntity[E](json: String)(implicit m: Manifest[E]): E = parse(json).extract[E]

  def extractEntityOpt[E](json: String)(implicit m: Manifest[E]) = parse(json).extractOpt[E]

  def extractEntityWithTry[E](json: String)(implicit m: Manifest[E]): Try[E] = Try(parse(json).extract[E] )

  def toJsonWithPreserve(value: JValue): String = {
    compact(render(value))
  }
}
object BaseJsonUtilities extends BaseJsonUtilitiesComponent with BaseTypeSerializers

object ll extends App {

  import BaseJsonUtilities._
  case class A(name:String,age:Int)
  val ll  = List(A("aamir",24),A("boy",22))
  println(ll.asJson)

}
