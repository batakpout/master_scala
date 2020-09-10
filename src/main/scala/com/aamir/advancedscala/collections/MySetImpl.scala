package advancedscala.collections

trait MySet[A] extends (A => Boolean) {

  def apply(elem: A): Boolean

  def contains(elem: A): Boolean

  def +(elem: A): MySet[A]

  def ++(anotherSet: MySet[A]): MySet[A] // union

  def map[B](f: A => B): MySet[B]

  def flatMap[B](f: A => MySet[B]): MySet[B]

  def filter(predicate: A => Boolean): MySet[A]

  def foreach(f: A => Unit): Unit

  def -(elem: A): MySet[A]

  def &(anotherSet: MySet[A]): MySet[A]

  def --(anotherSet: MySet[A]): MySet[A]

  def unary_! :MySet[A]
}

class EmptySet[A] extends MySet[A] {

  def apply(elem: A): Boolean = contains(elem)

  def contains(elem: A): Boolean = false

  def +(elem: A): MySet[A] = new NonEmptySet[A](elem, this)

  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

  def map[B](f: A => B): MySet[B] = new EmptySet[B]

  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]

  def filter(predicate: A => Boolean): MySet[A] = this

  def foreach(f: A => Unit): Unit = ()

  def -(elem: A): MySet[A] = this

  def &(anotherSet: MySet[A]): MySet[A] = this

  def --(anotherSet: MySet[A]): MySet[A] = this

  def unary_! : MySet[A] =  new PropertyBasedSet[A](_ => true)
}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {

  def apply(elem: A): Boolean = contains(elem)

  def contains(elem: A): Boolean = elem == head || tail.contains(elem)

  def +(elem: A): MySet[A] = if (this contains elem) this else new NonEmptySet[A](elem, this)

  def ++(anotherSet: MySet[A]): MySet[A] = tail ++ anotherSet + head

  def map[B](f: A => B): MySet[B] = new NonEmptySet[B](f(head), tail.map(f))

  def flatMap[B](f: A => MySet[B]): MySet[B] = f(head) ++ tail.flatMap(f)

  def filter(predicate: A => Boolean): MySet[A] = if (predicate(head)) new NonEmptySet[A](head, tail.filter(predicate)) else tail.filter(predicate)

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail foreach f
  }

  def -(elem: A): MySet[A] = if(head == elem) tail else tail - elem + head

  //override def &(anotherSet: MySet[A]): MySet[A] = filter(x => anotherSet.contains(x))
  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)

 // override def --(anotherSet: MySet[A]): MySet[A] = filter(x => !anotherSet.contains(x))

 // def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)
  override def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)

  def unary_! : MySet[A] = new PropertyBasedSet[A](x => !this.contains(x))
}

class PropertyBasedSet[A](property: A => Boolean) extends MySet[A] {

  override def apply(elem: A): Boolean = contains(elem)

  override def contains(elem: A): Boolean = property(elem)

  override def +(elem: A): MySet[A] = new PropertyBasedSet[A](x => property(x) || x == elem)

  override def ++(anotherSet: MySet[A]): MySet[A] = new PropertyBasedSet[A](x => property(x) || anotherSet(x))

  override def map[B](f: A => B): MySet[B] = fail

  override def flatMap[B](f: A => MySet[B]): MySet[B] = fail

  override def filter(predicate: A => Boolean): MySet[A] = new PropertyBasedSet[A](x => property(x) && predicate(x))

  override def foreach(f: A => Unit): Unit = fail

  override def -(elem: A): MySet[A] = filter(x => x != elem)

  override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)

  def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)

  def fail = throw new Exception

  override def unary_! : MySet[A] = filter(x => !property(x))
}

object MySet {
  def apply[A](values: A*): MySet[A] = {
    def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] = {
      if (valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)
    }

    buildSet(values.toSeq, new EmptySet[A])
  }
}

object MySetTest1 extends App {

  val s = MySet(1, 2, 3, 4)
  s foreach println
  println("-------11-----")
  val r1 = s + 5
  r1 foreach println
  println("-------22-----")
  val r2 = r1 ++ MySet(-1, -2)
  r2 foreach println
  println("-------33-----")
  val r3 = r2 + 3
  r3 foreach println
  println("-------44-----")
  val r4 = r3 + 4
  r4 foreach println
  println("-------55-----")
  val r5 = r4 flatMap (x => MySet(x, 10 * x))
  r5 foreach println
  println("-------66-----")
  val r6 = r5 filter (_ % 2 == 0)
  r6 foreach println
  println("-------77-----")
  r6 foreach println
}

/*object MySetTest2 extends App {

  def -(elem: A): MySet[A] = {
    //for construct runs through a map, so the yield will return what a map returns
    for {
      x <- this if x != elem
    } yield x
  }

  def --(anotherSet: MySet[A]): MySet[A] = {
    //remove all elements from this, which are in anotherSet
    for {
      x <- this if !(anotherSet contains x)
    } yield x
  }

  def &(anotherSet: MySet[A]): MySet[A] = {
    //returns only the elements that are common to both sets
    for {
      x <- this if anotherSet contains x
    } yield x
  }
}*/
object MySetTest3 extends App {

  val s1 = MySet(1, 2, 3, 4)
  val s2 = MySet(1, 44, 55, 4)

  val res = s1.--(s2)

  res foreach println

  println("-------------------")
  val res2 = res.-(2)
  res2 foreach println
}
object MySetPlayground extends App {
  val s = MySet(1,2,3,4)
  s + 5 ++ MySet(-1, -2) + 3 flatMap (x => MySet(x, 10 * x)) filter (_ % 2 == 0) foreach println


  val negative = !s // s.unary_! = all the naturals not equal to 1,2,3,4
  println(negative(2))
  println(negative(5))

  val negativeEven = negative.filter(_ % 2 == 0)
  println(negativeEven(5))

  val negativeEven5 = negativeEven + 5 // all the even numbers > 4 + 5
  println(negativeEven5(5))
}
