class Animal()
class Cat() extends Animal()
class Dog() extends Animal()
class Croc()



class Cage[-T](value: T)
//class Cage[-T](val value: T)

val c:Cage[Cat] = new Cage[Animal](new Dog())


/////////

class kitty extends Cat
class MyList[+T]{
  def add[T](element: T) : MyList[T] = new MyList[T]
}

val emptyList: MyList[kitty] = new MyList[kitty]
val animals: MyList[Cat] = emptyList.add(new Cat)
val another: MyList[Dog] = animals.add(new Dog)
animals.add(new Dog)
//function T shadows class T
