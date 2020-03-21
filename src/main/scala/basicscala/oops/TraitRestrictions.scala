package basicscala.oops

import basicscala.oops.CorrectLinearization.Swimmer

object TraitRestrictions1 extends App {

  /*class Omnivore

  class Mobility

  trait Swimmer extends Mobility

  //class Duck extends Omnivore fine
  class Duck extends Omnivore with Swimmer*/

  /**
   * Compile time error
   * Error:(12, 36) illegal inheritance; superclass Omnivore
   * is not a subclass of the superclass Mobility
   * of the mixin trait Swimmer
   * class Duck extends Omnivore with Swimmer
   */
}

object TraitRestrictions2 extends App {

    trait Movement

    trait Swimmer extends {
      def move = "swimming"
    }

    trait Flyer {
      def move = "flying"
    }

    //I mulitple traits define same memeber then we must use override...
    class Duck1 extends Swimmer with Flyer {
      override def move: String = super.move
    }

  class Duck2 extends Flyer with Swimmer {
    override def move: String = super.move
  }
  println(new Duck1().move)
  println(new Duck2().move)
  /**
   * if only    class Duck1 extends Swimmer with Flyer
   * Error:(32, 9) class Duck inherits conflicting members:
   * method move in trait Swimmer of type => String  and
   * method move in trait Flyer of type => String
   * (Note: this can be resolved by declaring an override in class Duck.)
   * class Duck extends Swimmer with Flyer
   */
}

object CorrectLinearization extends App {

  trait Movement {
    def move: String
  }

  trait Swimmer extends Movement {
    override def move = "swimming"
  }

  trait Flyer extends Movement {
    override def move = "flying"
  }

  class Duck extends Flyer with Swimmer


  var class_d = new Duck

  // whose property will be inherited
  println(class_d.move)


}



