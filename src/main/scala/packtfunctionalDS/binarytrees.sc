abstract sealed class Tree[+A](implicit ev: A => Ordered[A]) {
  def value: A

  def left: Tree[A]

  def right: Tree[A]

  def size: Int

  def isEmpty: Boolean

  def fail(m: String) = throw new NoSuchElementException(m)

  /**
   * Time - O(n)
   * Space - O(log n)
   */
  def isValid: Boolean = {
    if (isEmpty) true
    else if (left.isEmpty && right.isEmpty) true
    else if (left.isEmpty) right.value >= value && right.isValid
    else if (right.isEmpty) left.value <= value && left.isValid
    else left.value <= value && right.value >= value && left.isValid && right.isValid
  }

  /**
   * Time - O(log n)
   * Space - O(log n)
   */
  def add[B >: A](x: B)(implicit ev: B => Ordered[B]): Tree[B] = {
    if (isEmpty) Tree.make(x)
    else if (x < value) Tree.make(value, left.add(x), right)
    else if (x > value) Tree.make(value, left, right.add(x))
    else this
  }

  /**
   * Time - O(log n)
   * Space - O(log n)
   */
  def search[B >: A](x: B)(implicit ex: B => Ordered[B]): Boolean = {
    if (isEmpty) false
    else if (x == value) true
    else if (x < value) left.search(x)
    else right.search(x)
  }

  /**
   * Searches for the min element of this tree.
   *
   * Time - O(log n)
   * Space - O(log n)
   */
  def min: A = {
    def loop(t: Tree[A], x: A): A = {
      if (isEmpty) x else loop(t.left, x)
    }

    if (isEmpty) fail("An empty tree...")
    loop(left, value)
  }

  /**
   * Searches for the maximal element of this tree.
   *
   * Time - O(log n)
   * Space - O(log n)
   */
  def max: A = {
    def loop(t: Tree[A], x: A): A = {
      if (isEmpty) x else loop(t.right, x)
    }

    if (isEmpty) fail("An empty tree...")
    loop(right, value)
  }

  /**
   * Calculates the height of this tree.
   *
   * Time - O(n)
   * Space - O(log n)
   */

  def height: Int = 1 + left.height max right.height

  override def toString: String = if (isEmpty) "." else "{" + left + value + right + "}"
}

case class Branch[A](value: A, left: Tree[A],
                     right: Tree[A],
                     size: Int) extends Tree[A] {


  override def isEmpty: Boolean = false
}

case object Leaf extends Tree[Nothing] {
  def value: Nothing = fail("An empty tree.")

  def left: Tree[Nothing] = fail("An empty tree.")

  def right: Tree[Nothing] = fail("An empty tree.")

  def size: Int = 0

  def isEmpty: Boolean = true
}

object Tree {
  def make[A](value: A, l: Tree[A] = Leaf, r: Tree[A] = Leaf): Tree[A] =
    Branch(value, l, r, l.size + r.size + 1)

  def empty[A]: Tree[A] = Leaf

  def apply[A](xs: A*): Tree[A] = {
    xs.foldLeft(empty)((acc, x) => acc.add(x))
  }

  def complete(x: Int, d: Int): Tree[Int] = d match {
    case 0 => empty
    case _ => {
      make(x, complete(x * 2, d - 1), complete(x * 2 + 1, d - 1))
    }
  }

  def complete[A](x: A, d: Int): Tree[A] = d match {
    case 0 => empty
    case _ => make(x, complete(x, d - 1), complete(x, d - 1))
  }
}


