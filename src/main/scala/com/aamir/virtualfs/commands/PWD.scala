package virtualfs.commands
import virtualfs.filesystem.State

class PWD extends Command {
  override def apply(state: State): State = {
    state.setMessage(state.wd.path)
  }
}
