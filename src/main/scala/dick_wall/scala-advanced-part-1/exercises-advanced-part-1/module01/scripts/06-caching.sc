def fakeWeatherLookup(wxCode: String) = {
	Thread.sleep(1000)
	wxCode.toList.map(_.toInt).sum / 10.0
}

fakeWeatherLookup("PDX")

fakeWeatherLookup("SFO")

fakeWeatherLookup("PDX")

import com.google.common.cache.{CacheLoader, CacheBuilder}
import scala.concurrent._
import duration._
import ExecutionContext.Implicits.global


object FakeWeatherLookup {
	private val cache = CacheBuilder.newBuilder().
		build {
			new CacheLoader[String, Future[Double]] {
				def load(key: String) = Future(fakeWeatherLookup(key))
			}
		}

	def apply(wxCode: String) = cache.get(wxCode)
}

val f1 = FakeWeatherLookup("SFO")
val f2 = FakeWeatherLookup("SFO")

Await.result(f1, 10.seconds)
Await.result(f2, 10.seconds)
