import scala.annotation.tailrec

def checkPrimness(num: Int): Boolean = {
  @tailrec
  def isPrime(n: Int, isStillPrime: Boolean):Boolean = {
    if(!isStillPrime) false
    else if (n <= 2) true
    else isPrime(n - 1, (num % n != 0))
  }
  isPrime(Math.ceil(Math.sqrt(num)).toInt, true)
}
checkPrimness(629)

def nthFibonacci(num: Int) = {
  @tailrec
  def fibo(i: Int, last: Int, nextLast: Int):Int = {
    if(i >= num) last
    else fibo(i + 1, last + nextLast, last)
  }
  if(num <= 2) 1 else {
    fibo(2, 1, 1)
  }

}
nthFibonacci(8)
