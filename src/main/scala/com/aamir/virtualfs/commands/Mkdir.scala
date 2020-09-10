package virtualfs.commands

import virtualfs.files.{DirEntry, Directory}
import virtualfs.filesystem.State

class Mkdir(name: String) extends CreateEntry(name) {
  override def createSpecificEntry(state: State): DirEntry =
     Directory.empty(state.wd.path, name)
}