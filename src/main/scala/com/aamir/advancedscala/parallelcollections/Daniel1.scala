package com.aamir.advancedscala.parallelcollections


import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicReference
import scala.collection.parallel.CollectionConverters.ImmutableIterableIsParallelizable
import scala.collection.parallel.ForkJoinTaskSupport
import scala.collection.parallel.immutable.ParVector

object ParallelUtils extends App {

  // 1 - parallel collections

  val parList = List(1, 2, 3).par

  val aParVector = ParVector[Int](1, 2, 3)

  /*
    Seq
    Vector
    Array
    Map - Hash, Trie
    Set - Hash, Trie
   */
  def measure[T](operation: => T): Long = {
    val time = System.currentTimeMillis()
    operation
    System.currentTimeMillis() - time
  }

  val list = (1 to 10000000).toList
  val serialTime: Long = measure {
    list.map(_ + 1)
  }
  println("serial time: " + serialTime)

  val parallelTime = measure {
    list.par.map(_ + 1)
  }

  println("parallel time: " + parallelTime)

  //println(List(1,2,3).ch(_ - _))
  println(List(1,2,3).par.reduce(_ - _))

  // synchronization
  var sum = 0
  List(1,2,3).par.foreach(sum += _)
  println(sum)  // race conditions!

  // configuring
  aParVector.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(2))
  /*
    alternatives
    - ThreadPoolTaskSupport - deprecated
    - ExecutionContextTaskSupport(EC)
   */

  //  aParVector.tasksupport = new TaskSupport {
  //    override def execute[R, Tp](fjtask: Task[R, Tp]): () => R = ???
  //
  //    override def executeAndWaitResult[R, Tp](task: Task[R, Tp]): R = ???
  //
  //    override def parallelismLevel: Int = ???
  //
  //    override val environment: AnyRef = ???
  //  }

  // 2 - atomic ops and references

  // 2 - atomic ops and references

  val atomic = new AtomicReference[Int](2)

  val currentValue = atomic.get() // thread-safe read
  atomic.set(4) // thread-safe write

  atomic.getAndSet(5) // thread safe combo

  atomic.compareAndSet(38, 56)
  // if the value is 38, then set to 56
  // reference equality

  atomic.updateAndGet(_ + 1) // thread-safe function run
  atomic.getAndUpdate(_ + 1)

  atomic.accumulateAndGet(12, _ + _) // thread-safe accumulation
  atomic.getAndAccumulate(12, _ + _)
}