/**
 * Phantom Types, the reason why they are called phantom is that they are used as type constraints but never instantiated.
 */

trait Status
trait Open extends Status
trait Closed extends Status

trait Door[S <: Status]
object Door {
  def apply[S <: Status]: Door[S] = new Door[S] {}

  def open[S <: Closed](d: Door[S]): Door[Open] = Door[Open]
  def close[S <: Open](d: Door[S]): Door[Closed] = Door[Closed]
}

val closedDoor: Door[Closed] = Door[Closed]
val openDoor: Door[Open] = Door.open(closedDoor)
val closedAgainDoor = Door.close(openDoor)

// val closedClosedDoor = Door.close(closedDoor) RTE
// val openOpenDoor = Door.open(openDoor)