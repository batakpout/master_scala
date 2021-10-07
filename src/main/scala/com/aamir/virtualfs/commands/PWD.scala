package com.aamir.virtualfs.commands
import com.aamir.virtualfs.filesystem.State

class PWD extends Command {
  override def apply(state: State): State = {
    state.setMessage(state.wd.path)
  }
}
