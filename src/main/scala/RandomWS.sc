def sum(xs: List[Int]): Int = {

  def recursiveSum(xs: List[Int], acc: Int): Int = xs match {
    case Nil => acc
    case h :: t => recursiveSum(t, acc + h)
  }

  recursiveSum(xs, 0)

}

/**
 * This method returns the largest element in a list of integers. If the
 * list `xs` is empty it throws a `java.util.NoSuchElementException`.
 *
 * You can use the same methods of the class `List` as mentioned above.
 *
 * ''Hint:'' Again, think of a recursive solution instead of using looping
 * constructs. You might need to define an auxiliary method.
 *
 * @param xs A list of natural numbers
 * @return The largest element in `xs`
 * @throws java.util.NoSuchElementException if `xs` is an empty list
 */
def max(xs: List[Int]): Int = {

  def recursiveMax(xs: List[Int], maxElement: Int): Int = xs match {
    case Nil => maxElement
    case h :: t => {
      if (h > maxElement) recursiveMax(t, h) else recursiveMax(t, maxElement)
    }
  }

  if(xs.isEmpty) throw new NoSuchElementException
  recursiveMax(xs, 0)
}

sum(List(1,2,3,4,5))
max(List(-1, 33, 2, 76, 8))