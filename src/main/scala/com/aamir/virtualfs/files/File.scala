package com.aamir.virtualfs.files

import com.aamir.virtualfs.filesystem.FileSystemException

class File(override val parentPath: String, override val name: String, contents: String) extends DirEntry(parentPath, name) {
  override def asDirectory: Directory = throw new FileSystemException("A file cannot be converted to a directory")

  override def getType: String = "File"

  override def asFile: File = this
  def isDirectory: Boolean = false
  def isFile: Boolean = true
}

object File {

   def empty(parentPath: String, name: String) =
     new File(parentPath, name, "")
}