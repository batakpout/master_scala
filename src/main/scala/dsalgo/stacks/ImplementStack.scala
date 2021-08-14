package dsalgo.stacks

class MyStack[+A](self: List[A]) {

  def push[B >: A](elem: B) = new MyStack(elem :: self)

  def pop(): (Option[A], MyStack[A]) = self match {
    case Nil => (None, new MyStack(Nil))
    case _   => (Some(self.head), new MyStack(self.tail))
  }
  def peek: Option[A] = self.headOption
  def size: Int = self.size
  def isEmpty:Boolean = self.isEmpty

}

object MyStack {

  def empty[A]: MyStack[A] = new MyStack(Nil)

  def apply[A](xs: A*): MyStack[A] = {
    xs.foldLeft(MyStack.empty[A])((r, x) => r.push(x))
  }
}