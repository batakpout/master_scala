package Scala_Training



object Question_6 extends App {


  case class Endorsement(person: String, skill: String)
  case class SkillSummary(skill: String, people: List[String], count: Int)
  val endorsements = List(
    Endorsement("Bob", "JS"),
    Endorsement("David", "JS"),
    Endorsement("Bob", "HTML"),
    Endorsement("James", "HTML"),
    Endorsement("James", "JS"),
    Endorsement("James", "CSS") )
    .foldLeft(List[(String, String)]()) ( (acc: List[(String, String)],
                                           x: Endorsement) =>
      acc ++ List((x.skill, x.person)) ).groupBy(_._1).map(x => (x._1, x._2.map(_._2)))
    .toList.map(x => SkillSummary(x._1, x._2, x._2.length))

  println(endorsements)
}