package virtualfs.commands
import virtualfs.files.{DirEntry, File}
import virtualfs.filesystem.State

class Touch(name: String) extends CreateEntry(name) {
  override def createSpecificEntry(state: State): DirEntry =
    File.empty(state.wd.path, name)
}

