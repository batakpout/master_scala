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

object RockingInheritance_Linearization  extends App {

  trait Cold {
    def print = println("cold")
  }

  trait Green extends Cold {
      println("Green")
      super.print
  }

  trait Blue extends Cold {
      println("Blue")
      super.print
  }

  class Red {
    def print = println("red")
  }

  class White extends Red with Green with Blue {
    override def print: Unit = {
      println("white")
      super.print
    }
  }

  val color = new White
  /**
   *   Green extends Cold
   *   Blue extends Green extends Cold
   *   white extends Red with Green with Blue with cold
   */
  color.print
}