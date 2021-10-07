package com.aamir.virtualfs.files

import com.aamir.virtualfs.filesystem.FileSystemException

import scala.annotation.tailrec

class Directory(override val parentPath: String, override val name: String, val contents: List[DirEntry]) extends DirEntry(parentPath, name) {
  def hasEntry(name: String): Boolean = findEntry(name) != null


  def getAllFoldersInPath: List[String] =
    path.substring(1).split(Directory.SEPARATOR).toList.filter(x => x.nonEmpty)

  def findDescendant(path: List[String]): Directory = {
    if(path.isEmpty) this
    else findEntry(path.head).asDirectory.findDescendant(path.tail)
  }

  def addEntry(newEntry: DirEntry): Directory = {
    new Directory(parentPath, name, contents :+ newEntry)
  }

  def findEntry(name: String): DirEntry = {
    @tailrec
    def findEntryHelper(contentList: List[DirEntry]): DirEntry = {
      if (contentList.isEmpty) null
      else if (contentList.head.name.equals(name)) contentList.head
      else findEntryHelper(contentList.tail)
    }

    findEntryHelper(contents)
  }

  def replaceEntry(entryName: String, newEntry: DirEntry): Directory = {
    new Directory(parentPath, name, contents.filter(e => !e.name.equals(entryName)) :+ newEntry)
  }

  override def asDirectory: Directory = this

  override def getType: String = "Directory"

  override def asFile: File = throw new FileSystemException("A directory cannot be converted to a file")

  def isRoot:Boolean = parentPath.isEmpty
  def isDirectory: Boolean = true
  def isFile: Boolean = false
}

object Directory {
  val ROOT_PATH = "/"
  val SEPARATOR = "/"

  def ROOT: Directory = empty("", "")

  def empty(parentPath: String, name: String): Directory =
    new Directory(parentPath, name, Nil)
}
