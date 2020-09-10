class FullName(val first: String, val last: String)

object FullName {
  def apply(first: String, last: String): FullName = {
    new FullName(first, last)
  }

  def unapply(person: FullName): Option[(String, String)] = {
    Some(person.first, person.last)
  }
}

val me = FullName("Linas", "Medžiūnas")
val FullName(meFirst, meLast) = me

import java.time.{LocalDate, LocalDateTime, LocalTime}

object Date {
  def unapply(localData: LocalDate): Option[(Int, Int, Int)] = {
    Some((localData.getYear, localData.getMonthValue, localData.getDayOfMonth))
  }
}

object Time {
  def unapply(localTime: LocalTime): Option[(Int, Int, Int)] = {
    Some(localTime.getHour, localTime.getMinute, localTime.getMinute)
  }
}

object DateTime {
  def unapply(dt: LocalDateTime): Option[(LocalDate, LocalTime)] = {
    Some(dt.toLocalDate, dt.toLocalTime)
  }
}

val Date(year, month, day) = LocalDate.now
val Time(hour, minute, second) = LocalTime.now
println("*" * 50)
val DateTime(Date(y, m, d), Time(h, mm, s)) = LocalDateTime.now

val dt@DateTime(date@Date(y, m, d), time@Time(h, mi, s)) = LocalDateTime.now
println("-" * 50)

object DateTimeSeq {
  def unapplySeq(dt: LocalDateTime): Option[Seq[Int]] = {
    Some(Seq(dt.getYear, dt.getMonthValue, dt.getDayOfMonth,
      dt.getHour, dt.getMinute, dt.getSecond))
  }
}

val DateTimeSeq(y, m, d, h, mi, s) = LocalDateTime.now
val DateTimeSeq(y, m, d, h, _*) = LocalDateTime.now

object AM {
  def unapply(lt: LocalTime): Option[(Int, Int, Int)] = {
    lt match {
      case Time(h, m, s) if h < 12 => Some(h, m, s)
      case _                       => None
    }
  }
}

object PM {
  def unapply(lt: LocalTime): Option[(Int, Int, Int)] = {
    lt match {
      case Time(h, m, s) if h >= 12 => Some(h - 12, m, s)
      case _                       => None
    }
  }
}

LocalTime.now match {
  case AM(h,m,s) => s"its morning: $h:$m:$s"
  case PM(h,m,s) =>  s"its evening: $h:$m:$s"
}
LocalTime.now match {
  case t @ AM(h, m, _) =>
    f"$h%2d:$m%02d AM ($t precisely)"
  case t @ PM(h, m, _) =>
    s"$h:$m PM ($t precisely)"
}


val a = 4
val b = 25
val c= 100
scala.util.Try(s"$c/$a=$b")