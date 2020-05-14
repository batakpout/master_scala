import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
val f = Future{
  ""
}
val p = Promise[Int]
val f = p.future

p success(10)

case class TaxCut(reduction: Int)
val taxcut = Promise[TaxCut]()
val taxcut2: Promise[TaxCut] = Promise()
val taxcutF: Future[TaxCut] = taxcut.future

taxcut.success(TaxCut(20))

