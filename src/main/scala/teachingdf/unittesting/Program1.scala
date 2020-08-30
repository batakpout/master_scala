package teachingdf.unittesting

object Program1 {

  def getHospitalFirstMemeber(hospitalMembers: List[String]): String = {
    hospitalMembers.headOption.getOrElse("no memeber")
  }




}