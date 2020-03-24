package virtualfs.files

import virtualfs.filesystem.FileSystemException

class File(override val parentPath: String, override val name: String, contents: String) extends DirEntry(parentPath, name) {
  override def asDirectory: Directory = throw new FileSystemException("A file cannot be converted to a directory")

  override def getType: String = "File"

  override def asFile: File = ???
}
