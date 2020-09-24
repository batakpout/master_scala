/*
Contravariance, convariance, why method args can't be covariant and retrun contravairant

 def prepend[B >: A](element: B): MyList[B]

  //0(n)
  def append[B >: A](item: B): MyList[B]

   def filter(f: A => Boolean): MyList[A]

  def map[B](f: A => B): MyList[B]

  def flatMap[B](f: A => MyList[B]): MyList[B]

  def printElements: String

  override def toString: String = "[" + printElements + "]"

  Monday: Pattern matching
  Tueday: if possible: Parital functions
 */