
val xs = List(1,2,3)
val ys = List(1,2,3)

xs.filter(_ % 2 == 0)
for (x <- xs if x % 2 == 0) yield x

xs.flatMap(x => ys.map(y => (x, y)))
for (x <- xs; y <- ys) yield (x, y)

xs.map(_ + 1)
for(x <- xs) yield x + 1

//so,
for {
  x <- xs if x % 2 == 0
  y <- ys
} yield (x, y)

xs.filter { x =>
  x % 2 == 0
}.flatMap { x =>
   ys.map { y =>
     (x, y)
   }
}

def average(x: Int, xs: Int*): Double =
  (x :: xs.toList).sum.toDouble / (xs.size + 1)
val xss: List[Int] = List(1,2,3,4)
//Sometimes you want to supply each element of a list as many parameters.
// You can do that by adding a : _* type ascription to your list:


  average(1, xss: _*)

average(1,2,3,4,5)

class BankAccount {
  private var balance = 0
  def deposit(amount: Int): Int = {
    if (amount > 0) balance = balance + amount
    balance
  }
  def withdraw(amount: Int): Int =
    if (0 < amount && amount <= balance) {
      balance = balance - amount
      balance
    } else throw new Error("insufficient funds")
}
val obj = new BankAccount()
obj.deposit(100)
obj.withdraw(80)


