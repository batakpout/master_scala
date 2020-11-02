object DuplicateElementsList extends App{


  def repeatElementsInList[T](list: List[T],N: Int): List[T] = {
    list.flatMap (x =>
      List.fill(N)(x)
    )
  }
  val l1= repeatElementsInList(List(1,2,3),2)

  print(l1)




}

