import com.fasterxml.jackson.module.scala.deser.overrides.LazyList

import scala.#::

val tryRes = scala.util.Try(throw new Exception("peeeepeeee"))
val r: Int = tryRes.getOrElse(8)

