class Note(c: String)
val c = new Note("")

/*c match {
 // case Note(_) => println("")
}*/

//pattern matching doesn't work with normal classes
//A class can extend another class, whereas a case class can not extend another
// case class (because it would not be possible to correctly implement their equality).


