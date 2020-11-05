import java.util.UUID

case class Imper(str: String)
val i = Imper("com:mdsol:apps")
val s = "MWS 0a1721ba-d486-477e-80e4-195f61a27b72.value:signature"

val appUuid: String = s.split(":")(0).split(" ").last

val u: UUID = java.util.UUID.fromString(appUuid)

//val x: String = u.split(":")(0).split(" ").last

