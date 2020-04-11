package virtualfs.commands
import virtualfs.files.{DirEntry, Directory}
import virtualfs.filesystem.State

import scala.annotation.tailrec

class Cd(dir: String) extends Command {
  override def apply(state: State): State = {
     val root = state.root
     val wd = state.wd

    /**
     * 1. Find root
     * 2. Find the absolute path of the directory I want to cd to
     * 3. Find the directory to cd to, given the pat
     * 4. change the state given the new directory
     */
    val absolutePath = if(dir.startsWith(Directory.SEPARATOR)) {
      dir
    }
    else if(wd.isRoot) {
      val wdPath = wd.path
      wd.path + dir
    }
    else {
      wd.path + Directory.SEPARATOR + dir
    }

    val destinationDirectory: DirEntry = doFindEntry(root, absolutePath)

    if(destinationDirectory == null || !destinationDirectory.isDirectory)
      state.setMessage(s"$dir: no such directory")
    else {
      val s = State(root, destinationDirectory.asDirectory)
      s
    }
  }

  def doFindEntry(root: Directory, path: String): DirEntry = {

    @tailrec
    def findEntryHelper(currentDirectory: Directory, path: List[String]):DirEntry = {
      println(path + "ffff")
      if(path.isEmpty || path.head.isEmpty) {
        currentDirectory
      }
      else if(path.tail.isEmpty) {
        val x = currentDirectory.findEntry(path.head)
        x
      }
      else {
        val nextDir = currentDirectory.findEntry(path.head)
        if(nextDir == null || !nextDir.isDirectory) null
        else findEntryHelper(nextDir.asDirectory, path.tail)
      }
    }

    @tailrec
    def collapseRelativePath(path: List[String], result: List[String]): List[String] = {
      if(path.isEmpty) result
      else if(".".equals(path.head)) collapseRelativePath(path.tail, result)
      else if("..".equals(path.head)) {
          if(result.isEmpty) null
          else collapseRelativePath(path.tail, result.tail)
      } else collapseRelativePath(path.tail, result :+ path.head)
    }
    val tokens: List[String] = path.substring(1).split(Directory.SEPARATOR).toList
    //val newTokens: List[String] = collapseRelativePath(tokens, Nil)
    //if(newTokens == null)  null
    //else
    println(tokens + "dd")
    findEntryHelper(root, tokens)
  }

  /*
   * Relative path inputs:
   * [a, .] => [a]
   * [a,b,.,.] => [a,b]
   * [a,..] => []
   * [a,b,..] => [a]
   */

}