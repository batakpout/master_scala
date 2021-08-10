package dsalgo.stacks

class Stack[+A](self: List[A]) {

  def push[B >: A](elem: B) = new Stack(elem :: self)

  def pop(): (Option[A], Stack[A]) = self match {
    case Nil => (None, new Stack(Nil))
    case _   => (Some(self.head), new Stack(self.tail))
  }

  def peek: Option[A] = self.headOption

  def size: Int = self.size
  def isEmpty:Boolean = self.isEmpty

}

object Stack {

  def empty[A]: Stack[A] = new Stack(Nil)

  def apply[A](xs: A*): Stack[A] = {
    xs.foldLeft(Stack.empty[A])((r, x) => r.push(x))
  }
}