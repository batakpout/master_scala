package virtualfs.commands

import virtualfs.files.{DirEntry, Directory}
import virtualfs.filesystem.State

abstract class CreateEntry(name: String) extends Command {
  override def apply(state: State): State = {
    val wd = state.wd
    if (wd.hasEntry(name)) state.setMessage(s"Entry $name already exists...")
    else if (wd.name.contains(Directory.SEPARATOR)) state.setMessage(s"$name must not contain separators....")
    else if (checkIllegal(name)) state.setMessage(s"$name illegal entry name...")
    else doCreateEntry(state, name)
  }

  def checkIllegal(name: String): Boolean = name.contains(".")

  def doCreateEntry(state: State, name: String): State = {
/*
    if pwd is : /a/b, means first inside root we did mkdir a, mkdir b then we did cd b
    and now we are doing mkdir c inside b, then
    currentDirectory:==> State(Dir("","", [\,a,nil & \,b,Nil]))
    path: ==> [b, path is always w.r.t wd of state]
    newEntry: ==> Dir("\b", "c", Nil)
    output should be (as this method will return newRoot):==> Dir("", "", [\,a,Nil, \,b,[\,c,Nil]])
 */
    def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry): Directory = {
      if (path.isEmpty) currentDirectory.addEntry(newEntry) else {
        val oldEntry = currentDirectory.findEntry(path.head).asDirectory
        currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
      }
    }

    val wd: Directory = state.wd

    val allDirsInPath: List[String] = wd.getAllFoldersInPath
    val newEntry: DirEntry = createSpecificEntry(state)
    val newRoot: Directory = updateStructure(state.root, allDirsInPath, newEntry)
    val newWd: Directory = newRoot.findDescendant(allDirsInPath)
    val s = State(newRoot, newWd)
    s
  }

  def createSpecificEntry(state: State): DirEntry
}
