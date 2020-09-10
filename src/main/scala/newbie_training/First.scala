package newbie_training

object FirstProgram1 extends App {

     //values and variable types.

   //type inference
   val x = 22
   println(x)

   //vals are immutable
   //x = 10 // no reassignment for vals.

   val aString = "hello"
   println(aString)

   val aBoolean: Boolean = false
   val aChar: Char = 'a'
   val anInt: Int = x
   val aShort: Short = 4613
   val aLong: Long = 5273985273895237L
   val aFloat: Float = 2.0f
   val aDouble: Double = 3.14D

  //variable
   // WHY WE ALWAYS AVOID VARIABLES (var) in Scala
   var aVariable = 10
   println("aVariable" + aVariable)
   aVariable = 44
   println("aVariable" + aVariable)
}

object FirstProgram2 extends App {
   //Expressions/ Statements.
  val someValue = false

   val someOtherValue: Int = {
     if(someValue) 239 else 986
     42
   }

  println(someOtherValue)
}