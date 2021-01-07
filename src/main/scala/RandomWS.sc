val TDE_SYNC_INPUT_URL   = "tdeSyncInputUrl"
val TDE_ASYNC_INPUT_URL  = "tdeAsyncInputUrl"
val TDE_ASYNC_TARGET_URL = "tdeAsyncTargetUrl"
val OCR_INPUT_URL        = "ocrInputUrl"
val OCR_TARGET_URL       = "ocrTargetUrl"

val serviceUrlList = List(TDE_SYNC_INPUT_URL, TDE_ASYNC_INPUT_URL, TDE_ASYNC_TARGET_URL, OCR_INPUT_URL, OCR_TARGET_URL)

val map = Map("tdeSyncInputUrl" -> "ss", "tdeAsyncTargetUrl" -> "ssd")
serviceUrlList diff map.keySet.toList
