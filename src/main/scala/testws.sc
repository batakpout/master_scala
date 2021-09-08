//1,2,3,4,5,6,7
//7,5,6,1,4,2

val list = List(5,1,4,2)
//val l = List(1,2,3,4,5)

list.sum

def sum(arr: Array[Int]) = {
  val l  = arr.length + 1
  var arrSum = (l * (l + 1)) / 2
  arr foreach { elem =>
    arrSum = arrSum - elem
  }
  arrSum
}
sum(Array(7,5,1,4,2,3))

val l = List(1,2,3,4,5,6).filter(_ % 2 == 0).map(x => x * x)






