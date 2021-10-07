package com.aamir.virtualfs.commands

import com.aamir.virtualfs.files.{DirEntry, Directory}
import com.aamir.virtualfs.filesystem.State

class Mkdir(name: String) extends CreateEntry(name) {
  override def createSpecificEntry(state: State): DirEntry =
     Directory.empty(state.wd.path, name)
}