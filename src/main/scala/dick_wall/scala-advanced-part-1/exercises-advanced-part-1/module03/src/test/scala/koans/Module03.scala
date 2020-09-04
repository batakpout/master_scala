
/* Copyright (C) 2010-2017 Escalate Software, LLC All rights reserved. */

package koans

import org.scalatest.{FunSpec, Matchers, SeveredStackTraces}

class Module03 extends FunSpec with Matchers with SeveredStackTraces {

  // you probably wouldn't want to do this in reality, but it does show the flexibility of
  // abstract types. Provide a ReverserTrait that defines an abstract type (not parameterized),
  // and has a single abstract method called reverse.
  // Also provide one concrete method implementation defined only in ReverserTrait called canReverse that returns true
  // Then provide three concrete implementations to satisfy the tests below, uncomment the tests, and make sure they work.
  // BooleanReverser should take a boolean and return the logical not of that value
  // StringReverser should take a string and return the value of String.reverse
  // SeqReverser should take a Sequence and return the value of Seq.reverse

  // uncomment the tests below to make sure the traits and classes work as expected
  describe("ReverserTrait") {
    /*
    describe ("through BooleanReverser") {
      it ("should reverse Booleans to their logical not") {
        val boolReverse = new BooleanReverser
        boolReverse.isInstanceOf[ReverserTrait] should be (true)
        boolReverse.canReverse should be (true)
        assert(boolReverse.reverse(true) === false)
        assert(boolReverse.reverse(false) === true)
      }
    }

    describe ("through StringReverser") {
      it ("should reverse the order of strings") {
        val strReverse = new StringReverser
        strReverse.isInstanceOf[ReverserTrait] should be (true)
        strReverse.canReverse should be (true)
        assert(strReverse.reverse("reverse") === "esrever")
        assert(strReverse.reverse("Hello, World!") === "!dlroW ,olleH")
      }
    }

    describe ("through SeqReverser") {
      it ("should reverse the order of sequences") {
        val seqReverse = new SeqReverser
        seqReverse.isInstanceOf[ReverserTrait] should be (true)
        seqReverse.canReverse should be (true)
        assert(seqReverse.reverse(List(1,2,3)) === List(3,2,1))
        assert(seqReverse.reverse(List("does", "it", "work")) === List("work", "it", "does"))
      }
    }*/


    // now provide another implementation of ReverserTrait called IntReverser which reports false back to
    // the canReverse query, and throws an IllegalStateException if you try and reverse it
    // Uncomment the test below to make sure it works

    /* describe ("through IntReverser") {
      it ("should not allow reversal of an Int") {
        val intReverse = new IntReverser
        intReverse.isInstanceOf[ReverserTrait] should be (true)
        intReverse.canReverse should be (false)
        intercept[IllegalStateException] {
          intReverse.reverse(6) should not be (6)  // it should throw an exception instead!
        }
      }
    }*/
  }

  // create a trait called Ammunition that has an abstract method, spent, that returns a Boolean
  // and a val, weight, that returns an Int

  // Create a trait called RangeWeapon with an abstract SuitableAmmunition type that must be a kind of
  // Ammunition (from the trait above), a load method that takes that suitable kind
  // of ammunition and returns a Boolean
  // an abstract method shoot that returns an Optional instance of the ammunition, and another abstract method
  // weight of type Int

  // now create three ammunition types with the following characteristics:
  // Bullet, weight 1, spent is true if ever fired, false otherwise (implementation is up to you)

  // Bolt, weight 3, spent is always false

  // Charge, weight 0, spent is always true

  // and three RangeWeapons:
  // A SixShooter, that can be loaded up to 6 times, can only shoot bullets, and can only shoot them while
  // it has ammo left. When out, it will return None rather than Some(Bullet). Weight should be computed as 10
  // plus the weight of the number of bullets. If the gun is full (6 bullets already), do not add the bullet and return
  // false, otherwise return true. Shooting the gun should return a spent bullet and remove it from the ammo list

  // a Crossbow which has Bolt as its suitable ammo, has weight of 15 plus the weight of the bolt only if loaded,
  // returns Bolt from the shoot if there is a bolt to be fired, or None otherwise, may only be loaded with one
  // Bolt at a time, and returns false if you try and load it with another Bolt. Since Bolts may be re-used, there
  // is no need to change the state of spent (if you implemented spent in the best way for Bolt, you won't be able
  // to change it anyway - hint hint).

  // and a blaster, which has ammunition of Charge, always returns a weight of 5 (whether loaded or not). Any time
  // the load is called, the charger is recharged to full, and on a full charge it can shoot three shots. Shoot always
  // returns None (since there is nothing physical output from the gun firing, just energy). If you like, you can
  // print up Pew or Click to show whether the blaster fires. We will add a new method, empty, just for this type
  // so we can see when the blaster is empty. Loading the blaster with a charge, always returns true, even if it is
  // fully charged, but only allows a maximum of three shots still


  // Now uncomment the following helper method and tests to ensure compliance
  /* def checkWeaponWeight(weapon: RangeWeapon, weight: Int) = {
    assert(weapon.weight === weight)
  }

  describe ("A SixShooter") {
    val sixShooter = new SixShooter
    it ("should start empty") {
      sixShooter.shoot should be (None)
    }

    it ("should load up to six times, and only fire as many bullets as it had") {
      sixShooter.load(new Bullet) should be (true)
      sixShooter.load(new Bullet) should be (true)
      sixShooter.load(new Bullet) should be (true)
      sixShooter.load(new Bullet) should be (true)
      sixShooter.load(new Bullet) should be (true)
      sixShooter.load(new Bullet) should be (true)
      sixShooter.load(new Bullet) should be (false)

      // all bullets returned should be spent
      sixShooter.shoot.get.spent should be (true)
      sixShooter.shoot.get.spent should be (true)
      sixShooter.shoot.get.spent should be (true)
      sixShooter.shoot.get.spent should be (true)
      sixShooter.shoot.get.spent should be (true)
      sixShooter.shoot.get.spent should be (true)
      sixShooter.shoot should be (None)
    }

    it ("should get heavier as you add bullets, and lighter as you shoot them") {
      checkWeaponWeight(sixShooter, 10)
      sixShooter.load(new Bullet) should be (true)
      sixShooter.load(new Bullet) should be (true)
      checkWeaponWeight(sixShooter, 12)
      sixShooter.load(new Bullet) should be (true)
      checkWeaponWeight(sixShooter, 13)

      sixShooter.shoot
      sixShooter.shoot
      checkWeaponWeight(sixShooter, 11)
    }

    // the following should not compile if uncommented, please make sure it doesn't
    /* it ("should not allow the wrong kind of ammo") {
      sixShooter.load(new Bolt)
      sixShooter.load(new Charge)
    } */
  }

  describe ("A Crossbow") {
    val crossbow = new Crossbow

    it ("should start empty") {
      crossbow.shoot should be (None)
    }

    it ("should only load or shoot one bolt at a time") {
      crossbow.load(new Bolt) should be (true)
      crossbow.load(new Bolt) should be (false)

      val theBolt = crossbow.shoot.get
      theBolt.spent should be (false)
      crossbow.shoot should be (None)

      crossbow.load(theBolt) should be (true) // re-use the same bolt, since we can
      val nextBolt = crossbow.shoot.get
      nextBolt.spent should be (false)
      nextBolt should be (theBolt)
    }

    it ("should get heavier when loaded and lighter when not") {
      checkWeaponWeight(crossbow, 15)
      crossbow.load(new Bolt) should be (true)
      checkWeaponWeight(crossbow, 18)
      crossbow.load(new Bolt) should be (false)
      checkWeaponWeight(crossbow, 18)
      crossbow.shoot
      checkWeaponWeight(crossbow, 15)
      crossbow.shoot
      checkWeaponWeight(crossbow, 15)
    }

    // the following test should not compile if uncommented, please make sure it doesnt
    /* it ("should not allow the wrong kind of ammunition") {
      crossbow.load(new Bullet)
      crossbow.load(new Charge)
    } */
  }

  describe ("A Blaster") {
    val blaster = new Blaster

    it ("should start empty") {
      blaster.empty should be (true)
    }

    it ("should be able to load as many times as you like, but still only give three shots") {
      blaster.load(new Charge) should be (true)
      blaster.empty should be (false)
      blaster.shoot() should be (None)
      blaster.empty should be (false)
      blaster.shoot() should be (None)
      blaster.empty should be (false)
      blaster.shoot() should be (None)
      blaster.empty should be (true)

      blaster.load(new Charge) should be (true)
      blaster.load(new Charge) should be (true)
      blaster.load(new Charge) should be (true)
      blaster.load(new Charge) should be (true)
      blaster.empty should be (false)
      blaster.shoot() should be (None)
      blaster.empty should be (false)
      blaster.shoot() should be (None)
      blaster.empty should be (false)
      blaster.shoot() should be (None)
      blaster.empty should be (true)
    }

    it ("should not change weight depending on whether it is loaded or not") {
      blaster.empty should be (true)
      checkWeaponWeight(blaster, 5)
      blaster.load(new Charge) should be (true)
      blaster.empty should be (false)
      checkWeaponWeight(blaster, 5)
      blaster.load(new Charge) should be (true)
      blaster.empty should be (false)
      checkWeaponWeight(blaster, 5)
    }

    // the following test should fail to compile if uncommented, please check to make sure it does
    /* it ("should not accept the wrong kind of ammo") {
      blaster.load(new Bullet)
      blaster.load(new Bolt)
    } */
  }*/

  // Next, let's exercise our F bounded polymorphism chops. Alter the following class
  // so that it can be used in .sorted calls on a collection by implementing the
  // Ordered trait in its definition. Sort by year first and engine size next, both in
  // increasing order

  case class Car(name: String, year: Int, engineSizeCCs: Int) {
  }

  describe("Sorting cars by year and engine size") {
    it("should compile and sort correctly") {
      val car1 = Car("Grood", 1965, 1800)
      val car2 = Car("Frood", 1965, 2000)
      val car3 = Car("Shrood", 1965, 1500)
      val car4 = Car("Breg", 1963, 1000)
      val car5 = Car("Dreg", 1967, 2200)

      val cars = Vector(car1, car2, car3, car4, car5)

      //Uncomment these, then alter Car so that these compile and pass
      /*
      cars.sorted should be (Vector(car4, car3, car1, car2, car5))

      car2 should be > car1

      car3 should be < car5
      */
    }
  }
}


