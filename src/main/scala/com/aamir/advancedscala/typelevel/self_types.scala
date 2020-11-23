package com.aamir.advancedscala.typelevel

/**
 * Self-types are used where the types normally do not have any connection,
 * so that you shouldn't (or sometimes can't) enforce subtyping via extends
 */
object self_types extends App {

  // self_types: requires / enforces a type to be mixed in.

  trait Instrumentalist {
    def playInstrument(): Unit
  }

  trait Singer {
    self: Instrumentalist =>
    def sing(): Unit
  }

  trait LeadSinger extends Singer with Instrumentalist {
    override def sing(): Unit = ???

    override def playInstrument(): Unit = ???
  }

  /** Illegal inheritance , self type Vocalist does not conform to Instrumentalist */
  /*class Vocalist extends Singer {
    override def sing(): Unit = ???
  }*/

  val jamesHetfield = new Singer with Instrumentalist {
    override def sing(): Unit = ???

    override def playInstrument(): Unit = ???
  }

  class Guitarist extends Instrumentalist {
    override def playInstrument(): Unit = println("(guitar solo)")
  }

  //because Guitarist already extends Instrumentalist
  val ericClapton = new Guitarist with Singer {
    override def sing(): Unit = ???
  }

  // v/s inheritance
  class A

  class B extends A // B IS AN A

  trait T

  trait S {
    this: T =>
  } // S REQUIRES a T

  //CAKE PATTERN => "dependency injection"

  //DI
  class Component {
    //API
  }

  class ComponentA extends Component

  class ComponentB extends Component

  class DependentComponent(component: Component)

  //CAKE PATTERN

  trait ScalaComponent {
    //API
    def action(x: Int): String
  }

  trait ScalaDependentComponent {
    self: ScalaComponent =>
    def dependentAction(x: Int): String = action(x) + " this rocks!"
  }

  trait ScalaApplication {
    s: ScalaDependentComponent =>
  }

  //abstract layer 1
  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent

  //layer 2
  trait Profile extends ScalaDependentComponent with Picture
  trait Analytics extends ScalaDependentComponent with Stats

  //layer 3
  trait AnalyticsApp extends ScalaApplication with Analytics

  /** Difference between DI and cake pattern, Cake pattern dependencies are check at compile time,
   for di some framework or code take care of it's injection at runtime injected
   **/


  // cyclical dependencies

/*  trait A extends B
  trait B extends A*/

  //  class X extends Y
  //  class Y extends X

  trait X {
    self: Y =>
  }

  trait Y {
    self: X =>
  }


}

object self_types_tweet extends App {

  trait User {
    def name: String
  }

  trait Tweeter {
    self: User =>
    def tweet(msg: String): Unit
  }

  /* trait Wrong extends Tweeter {
     def noCanDo = name
   }*/

  trait SendTweet extends Tweeter with User {
    override def tweet(msg: String): Unit = println(s"$name: $msg")
  }

}

object self_type_structural_types extends App {

  /**
   *   self-types can specify non-class types. For instance:
   *   The self type here is a structural type. The effect is to say that anything that mixes in Foo
   *   must implement a no-arg "close" method returning unit. This allows for safe mixins for duck-typing.
   * */


  trait Tweet {
    self: { def whoTweets:String } =>
  }

  trait Tweeter extends Tweet {
    def whoTweets: String = "Aamir"
    def tweet = s"$whoTweets, some message"
  }

}