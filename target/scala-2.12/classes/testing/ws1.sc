def fact(n: Int): Int = {
  def rec(n: Int, acc: Int): Int = {
    if(n == 1) acc else rec(n - 1, n * acc)
  }
  rec(n, 1)
}

fact(5)


def m(f: Int => Int, g: (Int, Int) => Int, num: Int):Int = {
  if(num == 1) f(num)
  else  g(m(f, g, num - 1), f(num))
}

/**
 * num = 5 , f(1), ....f(5) ,
 * g(g(g(g(f(1), f(2))), f(3)), f(4)), f(5))
 */

  /**
   * 1,1,2,3,5,8,13,
   */

def fibo(n: Int):Int = n match {
  case 0 | 1 => n
  case _ => fibo(n - 1) + fibo(n - 2)
}
fibo(510)