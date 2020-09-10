package virtualfs.commands

import virtualfs.filesystem.State

class UnknownCommand extends Command {
  def apply(state: State): State = {
    state.setMessage("Command not found")
  }
}
