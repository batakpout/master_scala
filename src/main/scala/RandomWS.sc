val howManyInput: Int = 1
val howManyListItem: Int = 3

case class Row(id: Int, list: List[Int])


val input: List[Row] = (0 to howManyInput)
  .map(i => Row(i, list = (0 to howManyListItem).toList))
  .toList

val modFilter: Int = 2
val increase: Int = 1

val output1: List[Int] = input.flatMap {
  case Row(id, list) =>
    list.flatMap(n =>
      List(id, n)
        .withFilter(i => id + n % modFilter == 0)
        .map(_ + increase)
    )
}

List(1,2,3).withFilter(_ % 2 ==0).map(_ + 1)