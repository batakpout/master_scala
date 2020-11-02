//Duplicate elements of a list//
//input = List(1,2,3) or List('a', 'b') or List of anything, you should create one generic method,
//which should take list of any type.//
//output = List(1, 1, 2, 2, 3, 3) or List('a', 'a', 'b', 'b')



package week1

object question_4 extends App{
  def duplicateElementList [D](list: List[D],N: Int): List[D] = {
    list.flatMap (x =>
      List.fill(N)(x)
    )
  }
  val l1= duplicateElementList(List(1,2,3),2)
  println(l1)

  val l2 = duplicateElementList(List('a','b'),2)
  println(l2)



}
