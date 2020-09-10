
/* Copyright (C) 2010-2014 Escalate Software, LLC. All rights reserved. */

package koans

import com.google.common.cache.{CacheLoader, CacheBuilder}
import org.scalatest.Matchers
import support.BlankValues._
import support.KoanSuite

import scala.concurrent._
import duration._
import ExecutionContext.Implicits.global


class Module01Solutions extends KoanSuite with Matchers {

  class CalorieTracker {
    private[this] var _dailyMax: Int = _
    private[this] var _currentDaily: Int = 0

    def dailyMax: Int = _dailyMax
    def dailyMax_=(x: Int) {
      require(x > 0 && x < 5000, "Out of allowable calorific range")
      _dailyMax = x
    }

    def currentDaily: Int = _currentDaily
    def currentDaily_=(x: Int) {
      require(x > -500 && x < (_dailyMax + 500), "May not deviate from daily range by 500 or more calories")
      _currentDaily = x
    }

    def eat(cals: Int) { currentDaily -= cals }
    def exercise(cals: Int) { currentDaily += cals }
  }

  test ("should be able to set daily and current calorie values") {
    val tracker = new CalorieTracker
    tracker.dailyMax = 2500 // should be fine
    tracker.currentDaily = 1200 // should also be fine
    tracker.dailyMax should be (2500)
    tracker.currentDaily should be (1200)
  }

  test ("should enforce limits on daily") {
    val tracker = new CalorieTracker
    intercept[IllegalArgumentException] { tracker.dailyMax = 0 }
    intercept[IllegalArgumentException] { tracker.dailyMax = -100 }
    intercept[IllegalArgumentException] { tracker.dailyMax = 5000 } // should allow up to 4999 calories only
  }

  test ("should enforce limits on setting current daily calories based on tracker max") {
    val tracker1 = new CalorieTracker
    val tracker2 = new CalorieTracker

    tracker1.dailyMax = 2000
    tracker2.dailyMax = 3000

    tracker1.currentDaily = 1500
    tracker2.currentDaily = 1500

    tracker1.dailyMax should be (2000)
    tracker2.dailyMax should be (3000)
    tracker1.currentDaily should be (1500)
    tracker2.currentDaily should be (1500)

    // now lets add some earned calories
    intercept[IllegalArgumentException] { tracker1.currentDaily += 1000 }
    tracker2.currentDaily += 1000

    tracker1.currentDaily should be (1500)
    tracker2.currentDaily should be (2500)
  }

  test ("should enforce limits on the eat and exercise methods too") {
    val tracker1 = new CalorieTracker
    val tracker2 = new CalorieTracker

    tracker1.dailyMax = 2000
    tracker2.dailyMax = 3000

    tracker1.currentDaily = 1500
    tracker2.currentDaily = 1500

    tracker1.dailyMax should be (2000)
    tracker2.dailyMax should be (3000)
    tracker1.currentDaily should be (1500)
    tracker2.currentDaily should be (1500)

    // now lets add some earned calories
    intercept[IllegalArgumentException] { tracker1.exercise(1000) }
    tracker2.exercise(1000)

    tracker1.currentDaily should be (1500)
    tracker2.currentDaily should be (2500)

    tracker1.eat(1000)
    tracker2.eat(1000)

    tracker1.currentDaily should be (500)
    tracker2.currentDaily should be (1500)

    intercept[IllegalArgumentException] { tracker1.eat(1000) }
    tracker2.eat(1000)

    tracker1.currentDaily should be (500)
    tracker2.currentDaily should be (500)
  }


  case class RecipesForFood(food: String)

  object SlowDB {
    val foods = Map("eggs" -> List("ommelette", "french toast", "poached eggs"),
      "bread" -> List("beans on toast", "french toast", "marmite sandwich"),
      "peanuts" -> List("peanut butter", "trail mix", "pad thai"),
      "ground beef" -> List("chili", "meat sauce", "swedish meatballs"))

    def findRecipes(food: String): List[String] = {
      Thread.sleep(1000); foods.getOrElse(food, Nil)
    }
  }

  object DBCache {
    val recipeCache = CacheBuilder.newBuilder().build(
      new CacheLoader[String, Future[List[String]]] {
        def load(key: String) = Future(SlowDB.findRecipes(key))
      }
    )


    def recipesForFood(food: String): Future[List[String]] = {
      recipeCache.get(food)
    }
  }

  test ("cache should not take a long time to retrieve recipes for the same food") {

    val startTime = System.currentTimeMillis

    val eggRecipes: Future[List[String]] = DBCache.recipesForFood("eggs")
    val eggRecipes2: Future[List[String]] = DBCache.recipesForFood("eggs")
    val eggRecipes3: Future[List[String]] = DBCache.recipesForFood("eggs")

    println("Back in %d millis".format(System.currentTimeMillis - startTime))
    if ((System.currentTimeMillis - startTime) > 1000) fail("Took too long to do lookup from DB")

    // now check that the futures are all the same for the egg recipes (should be the same instance)
    assert(eggRecipes eq eggRecipes2)
    assert(eggRecipes eq eggRecipes3)

    // and getting the lists from all three of the egg recipes should take no more than around 1 second
    Await.result(eggRecipes, 10.seconds) should be (List("ommelette", "french toast", "poached eggs"))
    Await.result(eggRecipes2, 10.seconds) should be (List("ommelette", "french toast", "poached eggs"))
    Await.result(eggRecipes3, 10.seconds) should be (List("ommelette", "french toast", "poached eggs"))

    println("Total time %d millis".format(System.currentTimeMillis - startTime))
    if ((System.currentTimeMillis - startTime) > 1500) fail("Took too long, must be doing more than one DB lookup for eggs")

  }
}
