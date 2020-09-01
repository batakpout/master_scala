package advancedscala.typelevel.typesystem

import scala.collection.mutable.ArrayBuffer

object StackableModifications1 extends App {

  abstract class IntQueue {
    def get(): Int
    def put(x: Int): Unit
  }

  class BasicQueue extends IntQueue {
    private val buf = new ArrayBuffer[Int]()
    override def get(): Int = buf.remove(0)

    override def put(x: Int): Unit = buf += x
  }

  trait Doubling extends IntQueue {
    abstract override def put(x: Int): Unit =
      super.put(2 * x)
  }

  val bQ = new BasicQueue with Doubling
  bQ.put(1)
  bQ.put(2)

  println(bQ.get())
  println(bQ.get())

  println("---------------")

  trait Filtering extends IntQueue {
    abstract override def put(x: Int): Unit = if(x >=  0) super.put(x)
  }

  trait Incrementing extends IntQueue {
    abstract override def put(x: Int): Unit = super.put(x + 1)
  }

  //Filtering, Incrementing can only be mixin with class that also extends IntQueue
  val bQ2 = new BasicQueue with Incrementing with Filtering
  bQ2.put(1)
  bQ2.put(-1)
  bQ2.put(2)

  println(bQ2.get())
  println(bQ2.get())

}