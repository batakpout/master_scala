/*
package newbie_training.assign.pooja

import collection.mutable.{HashMap, MultiMap, Set}

import scala.collection.immutable.HashMap
object Six extends App{
    val endorsements = List( ("Bob", "JS"), ("David", "JS"), ("Bob", "HTML"), ("James", "HTML"),("James", "JS"),("James", "CSS") )
    val multiM = new HashMap[String, Set[String]] with MultiMap[String, String]
    endorsements.foreach { case (key, value) => multiM.addBinding(value, key) }
    // println(multiM)
    multiM.keys.foreach{i=>println("Skill: "+ i+" People: "+multiM(i).toList + " Count:" + multiM(i).size)}

}
*/
