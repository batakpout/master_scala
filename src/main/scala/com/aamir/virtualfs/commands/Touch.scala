package com.aamir.virtualfs.commands
import com.aamir.virtualfs.files.{DirEntry, File}
import com.aamir.virtualfs.filesystem.State

class Touch(name: String) extends CreateEntry(name) {
  override def createSpecificEntry(state: State): DirEntry =
    File.empty(state.wd.path, name)
}

