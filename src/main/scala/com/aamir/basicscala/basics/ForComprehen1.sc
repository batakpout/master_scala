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
        .withFilter{ x =>
          println("x : " + x)
          println("id : " + id)
          println("n : " + n)
          id + (n % modFilter) == 0}
        .map(_ + increase)
    )
}

/**
 *List(id, n)  => List(0,0),List(0,1),List(0,2),List(0,3)
 * List(id, n) =>  List(1,0),List(1,1),List(1,2),List(1,3)
 * Result: List(0, 0,0, 2)
 */

for {
  Row(id, list) <- input
  listItem <- list
  i  <- List(id, listItem)
  if id + listItem % modFilter == 0
} yield {
  i + increase
}