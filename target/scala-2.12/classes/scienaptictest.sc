case class Endorsement(name: String, skill: String)

case class Result(people: List[String], count: Int)
case class FinalResult(skill: String, people: List[String], count: Int)

val endorsements = List(
  Endorsement("Bob", "JS"),
  Endorsement("David", "JS"),
  Endorsement("Bob", "HTML"),
  Endorsement("James", "HTML"),
  Endorsement("James", "JS"),
  Endorsement("James", "CSS")
)

// Output representation in JSON.

/*
  [
    {skill: 'JS', people:['Bob', 'David', 'James'], count:3},
    {skill: 'HTML', people:['Bob', 'James'], count:2},
    {skill: 'CSS', people:['James'], count:1},
  ]*/
endorsements.groupBy(_.skill)

def cal2(end: List[Endorsement]): List[(String, FinalResult)] = {
  end.foldLeft(Map.empty[String, FinalResult]) { (a, b) =>
    val x = a.get(b.skill)
    if (x.isDefined) {
      val newList: List[String] = b.name :: x.get.people
      a ++ Map((b.skill -> FinalResult(b.skill, newList, newList.size)))
    } else {
      val newList = List(b.name)
      a ++ Map((b.skill -> FinalResult(b.skill, newList, newList.size)))
    }
  }.toList
}

cal2(endorsements)
//check series....



