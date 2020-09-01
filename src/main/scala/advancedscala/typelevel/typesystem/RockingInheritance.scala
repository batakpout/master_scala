package advancedscala.typelevel.typesystem

object RockingInheritance extends App {

   trait Writer[T] {
     def write(value: T): Unit
   }

   trait Closeable{
     def close(status: Int): Unit
   }

  trait GenericStream[T] {
    def foreach(f: T => Unit): Unit
  }

  def processStream[T](stream: GenericStream[T] with Closeable with Writer[T]):Unit = {
    stream.foreach(_ => println())
    stream.close(0)
  }

  //Diamond problem
  trait Animal { def name: String}
  trait Lion extends Animal{ override def name: String = "lion"}
  trait Tiger extends Animal{ override def name: String = "tiger"}
  class Mutant extends Tiger with Lion

  println(new Mutant().name)
}