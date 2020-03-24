package virtualfs.commands

import virtualfs.files.{DirEntry, Directory}
import virtualfs.filesystem.State

class Mkdir(name: String) extends Command {
  override def apply(state: State): State = {
    val wd = state.wd
    if (wd.hasEntry(name)) state.setMessage(s"Entry $name already exists...")
    else if (wd.name.contains(Directory.SEPARATOR)) state.setMessage(s"$name must not contain separators....")
    else if (checkIllegal(name)) state.setMessage(s"$name illegal entry name...")
    else doMkdir(state, name)
  }

  def checkIllegal(name: String): Boolean = name.contains(".")

  def doMkdir(state: State, name: String): State = {

    def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry): Directory = {
      if (path.isEmpty) currentDirectory.addEntry(newEntry) else {
        val oldEntry = currentDirectory.findEntry(path.head).asDirectory
        currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
      }
    }

    val wd: Directory = state.wd

    val allDirsInPath: List[String] = wd.getAllFoldersInPath
    val newDir: Directory = Directory.empty(wd.path, name)
    val newRoot: Directory = updateStructure(state.root, allDirsInPath, newDir)
    val newWd: Directory = newRoot.findDescendant(allDirsInPath)
    val s = State(newRoot, newWd)
    s
  }
}