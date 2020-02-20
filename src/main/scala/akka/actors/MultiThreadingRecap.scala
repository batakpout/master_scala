package akka.actors

object MultiThreadingRecap extends App {

  val aThread = new Thread(new Runnable {
    override def run(): Unit =
      println("I am running in parallel")
  })
  aThread.start()
  aThread.join()

  val helloThread: Thread = new Thread(() => {
    (1 to 100).foreach(_ =>println("Hello"))
  })

  val threadGoodBye = new Thread(() => {
    (1 to 100).foreach(_ => println("Good-bye"))
  })

  helloThread.start()
  threadGoodBye.start()
}

class BankAccount(var amount: Long) {
  override def toString: String = s"Amount: $amount"

  def withdraw(money: Long) = this.synchronized{
    amount -= money
  }
}
//inter thread communication on the jvm
//wait and notify

//from FP point of view, Future is a monodic construct, means it has functional primitives