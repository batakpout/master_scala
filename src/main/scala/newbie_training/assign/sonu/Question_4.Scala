object Question_4 extends App{
def duplicateN2[A](n: Int, l: List[A]):List[A] = {
  l flatMap { List.fill(n)(_) }
}
duplicateN2(3,List(1,2,3))
}