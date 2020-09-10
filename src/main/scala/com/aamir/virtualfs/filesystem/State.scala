package virtualfs.filesystem
import virtualfs.files.Directory
class State(val root: Directory, val wd: Directory, output: String) {

   def show = {
     println(output)
     println(State.SHELL_TOKEN)
   }

  def setMessage(message: String): State =
    State(root, wd, message)
}

object State {
  val SHELL_TOKEN = "$ "

  def apply(root: Directory, wd: Directory, output: String = "") =
    new State(root, wd, output)
}