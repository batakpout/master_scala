//https://stackoverflow.com/questions/5270752/difference-between-case-object-and-object
case object A
object B

import java.io._

val bos = new ByteArrayOutputStream()
val oos = new ObjectOutputStream(bos)
oos.writeObject(A)

//oos.writeObject(B) java.io.NotSerializableException: B$

A.isInstanceOf[Serializable]
B.isInstanceOf[Serializable]

A.toString
B.toString