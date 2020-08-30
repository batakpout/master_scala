package advancedscala.typelevel.typeclasses

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
//http://blog.madhukaraphatak.com/scala-magnet-pattern/
object MagnetPattern extends App {

 /* def completeFuture(value:Future[String]):String = Await.result(value,Duration.Zero)
  def completeFuture(value: Future[Int]):Int =  Await.result(value,Duration.Zero)

  completeFuture( Future {1}) // returns value 1
  completeFuture( Future{"hello"}) // return value "hello"*/

  sealed trait FutureMagnet {
    type Result

    def apply() : Result
  }
  def completeFuture(magnet: FutureMagnet):magnet.Result = magnet()

  object FutureMagnet {
    implicit def intFutureCompleter(future:Future[Int]) = new FutureMagnet {
      override type Result = Int

      override def apply(): Result = Await.result(future, Duration.Zero)
    }

    implicit def stringFutureCompleter(future:Future[String]) = new FutureMagnet {
      override type Result = String

      override def apply(): Result = Await.result(future,Duration.Zero)
    }

  }

/*  implicit class intFutureCompleter(future: Future[Int]) extends FutureMagnet {
    override type Result = Int

    override def apply() = Await.result(future, Duration.Zero)
  }*/

  println( completeFuture( Future {1}) )
  println( completeFuture( Future{"hello"}) )

}
object MagnetPattern1 extends App {

  // method overloading

  class P2PRequest
  class P2PResponse
  class Serializer[T]


/*  trait Actor {
    def receive(statusCode: Int): Int
    def receive(request: P2PRequest): Int
    def receive(response: P2PResponse): Int
    def receive[T : Serializer](message: T): Int
    def receive[T](message: T)(implicit ser: Serializer[T]): Int
    def receive[T : Serializer](message: T, statusCode: Int): Int
    def receive(future: Future[P2PRequest]): Int
    //def receive(future: Future[P2PResponse]): Int
    // lots of overloads
  }*/

  /*
  1 - type erasure
  2 - lifting doesn't work for all overloads
    val receiveFV = receive _ // ?!
  3 - code duplication
  4 - type inferrence and default args
    actor.receive(?!)
 */

  trait MessageMagnet[Result] {
    def aa(): Result
  }

  def receive[R](magnet: MessageMagnet[R]):R = magnet.aa()

  implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int] {
    def aa(): Int = {
      // logic for handling a P2PRequest
      println("Handling P2P request")
      42
    }
  }

  implicit class FromP2PResponse(response: P2PResponse) extends MessageMagnet[Int] {
    def aa(): Int = {
      // logic for handling a P2PResponse
      println("Handling P2P response")
      24
    }
  }

  println( receive(new P2PRequest))
  println(receive(new P2PResponse))
}
object MagnetPattern2 extends App {
  // 2 - lifting works
  trait MathLib {
    def add1(x: Int): Int = x + 1
    def add1(s: String): Int = s.toInt + 1
    // add1 overloads
  }

  // "magnetize"
  trait AddMagnet {
    def apply(): Int
  }

  def add1(magnet: AddMagnet): Int = magnet()

  implicit class AddInt(x: Int) extends AddMagnet {
    override def apply(): Int = x + 1
  }

  implicit class AddString(s: String) extends AddMagnet {
    override def apply(): Int = s.toInt + 1
  }

  val addFV: AddMagnet => Int = add1 _
  println(addFV(1))
  println(addFV("3"))

   //val receiveFV = add1 _
  //  receiveFV(new P2PResponse)
}
object MagnetPattern3 extends App {
  /*
   Drawbacks
   1 - verbose
   2 - harder to read
   3 - you can't name or place default arguments
   4 - call by name doesn't work correctly
   (exercise: prove it!) (hint: side effects)
  */

  trait HandleMagnet  {
    def apply(): Unit
  }

  def handle(magnet: HandleMagnet) = magnet()

  implicit class StringHandle(s: => String) extends HandleMagnet {
    override def apply(): Unit = {
      println(s)
      println(s)
    }
  }

  def sideEffectMethod(): String = {
    println("Hello, Scala")
    "hahaha"
  }

    //handle(sideEffectMethod())
  handle {
    println("Hello, Scala")
    "magnet"
  }
  // careful!
}