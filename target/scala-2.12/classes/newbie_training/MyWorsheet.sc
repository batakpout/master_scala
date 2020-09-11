def x(i: => Int)
{
  println(i)
  println(i)
  println(i)
  println(i)
}

var Total = 0;

// calling function
x {
  Total += 1
  Total
}