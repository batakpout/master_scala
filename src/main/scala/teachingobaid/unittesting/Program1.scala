package teachingobaid.unittesting

object Program1 {

  def getHospitalFirstMemeber(hospitalMembers: List[String]): String = {
    hospitalMembers.headOption.getOrElse("no memeber")
  }


  def m1() = {

    val x= 10
    val l = List(1)
    val y = l.last
    val res = m2()
    val ee = res

  }

  //val res = when(m2 is called).then(10)
  //res = 10

  def m2() = {
    val x= 10
    val l = List(1)
    val y = l.last
    val res = m3()
    val ee = res
  }

  def m3() = {
    val x= 10
    val l = List(1)
    val y = l.last
    val res = m2()
    val ee = res
  }

}