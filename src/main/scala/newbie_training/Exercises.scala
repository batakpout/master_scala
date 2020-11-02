/*
package newbie_training

trait NewbieList[+T] {

  def head: T
  def tail: NewbieList[T]
  def filter(f: T => Boolean): List[T]
  def map(f: T => T): List[T]
  def flatMap(f: T => List[T]): List[T]
  def makeString(): String
  def isEmpty:Boolean

}

class NonEmptyNewbieList[T](h: T, t: NewbieList[T]) extends NewbieList[T] {
  override def head: T = h

  override def tail: NewbieList[T] = t

  override def filter(f: T => Boolean): List[T] = ???

 // override def map(f: T => T): List[T] = ???

  override def makeString(): String = ???

  override def isEmpty: Boolean = ???

 // override def flatMap(f: T => List[T]): List[T] = ???
}

class EmptyNewbieList extends NewbieList[Nothing] {
  override def head: Nothing = ???

  override def tail: Nothing = ???

  override def filter(f: Nothing => Boolean): List[Nothing] = ???

  //override def map(f: Nothing => Nothing): List[Nothing] = ???

  override def makeString(): String = ???

  override def isEmpty: Boolean = ???

 // override def flatMap(f: Nothing => List[Nothing]): List[Nothing] = ???
}
*/
