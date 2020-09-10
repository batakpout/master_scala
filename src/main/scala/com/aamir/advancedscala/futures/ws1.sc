import scala.concurrent.{Future, Await}
import scala.util.{Failure, Success, Random}
import scala.concurrent.duration._

def calculateMeaningOfLife: Int = {
  Thread.sleep(2000)
  42
}
import scala.concurrent.ExecutionContext.Implicits.global

val aFuture = Future {
  calculateMeaningOfLife // calculates the  meaning of  life on ANOTHER thread
} // (global) which is passed by the compiler
//Thread.sleep(9000)
aFuture.value// Option[Try[Int]]
println("Waiting on the future")

aFuture.onComplete {
  case Success(meaningOfLife) => println(s"the meaning of life is $meaningOfLife")
  case Failure(e) => println(s"I have failed with $e")
} // SOME thread

Thread.sleep(3000)

// mini social network
case class Profile(id: String, name: String) {
  def poke(anotherProfile: Profile) =
    println(s"${this.name} poking ${anotherProfile.name}")
}

object SocialNetwork {
  // "database"
  val names = Map(
    "fb.id.1-zuck" -> "Mark",
    "fb.id.2-bill" -> "Bill",
    "fb.id.0-dummy" -> "Dummy"
  )
  val friends = Map(
    "fb.id.1-zuck" -> "fb.id.2-bill"
  )

  val random = new Random()
  // API
  def fetchProfile(id: String): Future[Profile] = Future {
    // fetching from the DB
    Thread.sleep(random.nextInt(300))
    Profile(id, names(id))
  }

  def fetchBestFriend(profile: Profile): Future[Profile] = Future {
    Thread.sleep(random.nextInt(400))
    val bfId = friends(profile.id)
    Profile(bfId, names(bfId))
  }
}

val mark = SocialNetwork.fetchProfile("fb.id.1-zuck")
  mark.onComplete {
    case Success(markProfile) => {
      val bill = SocialNetwork.fetchBestFriend(markProfile)
      bill.onComplete {
        case Success(billProfile) => markProfile.poke(billProfile)
        case Failure(e) => e.printStackTrace()
      }
    }
    case Failure(ex) => ex.printStackTrace()
  }

Thread.sleep(5000)

// functional composition of futures
// map, flatMap, filter
val nameOnTheWall: Future[String] = mark.map(profile => profile.name)
val marksBestFriend: Future[Profile] = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))
val zucksBestFriendRestricted: Future[Profile] = marksBestFriend.filter(profile => profile.name.startsWith("Z"))

val res: Unit = for {
  mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
  bill <- SocialNetwork.fetchBestFriend(mark)
} mark.poke(bill)

// fallbacks
val aProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recover {
  case e: Throwable => Profile("fb.id.0-dummy", "Forever alone")
}

val aFetchedProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recoverWith {
  case e: Throwable => SocialNetwork.fetchProfile("fb.id.0-dummy")
}

val fallbackResult =  SocialNetwork.fetchProfile("unknown id").fallbackTo(SocialNetwork.fetchProfile("fb.id.0-dummy"))
// online banking app

case class User(name: String)
case class Transaction(sender: String, receiver: String, amount: Double, status: String)
object BankingApp {
  val name = "Rock the JVM banking"

  def fetchUser(name: String): Future[User] = Future {
    // simulate fetching from the DB
    Thread.sleep(500)
    User(name)
  }

  def createTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] = Future {
    // simulate some processes
    Thread.sleep(1000)
    Transaction(user.name, merchantName, amount, "SUCCESS")
  }

  def purchase(username: String, item: String, merchantName: String, cost: Double): String = {
    // fetch the user from the DB
    // create a transaction
    // WAIT for the transaction to finish
    val transactionStatusFuture = for {
      user <- fetchUser(username)
      transaction <- createTransaction(user, merchantName, cost)
    } yield transaction.status
    import akka.util.Timeout
    //implicit val timeout=Timeout(2.seconds)
    //Await.result(transactionStatusFuture, timeout.duration) // implicit conversions -> pimp my library
    Await.result(transactionStatusFuture, 2.seconds) // implicit conversions -> pimp my library
  }
}

println(BankingApp.purchase("Daniel", "iPhone 12", "rock the jvm store", 3000))

