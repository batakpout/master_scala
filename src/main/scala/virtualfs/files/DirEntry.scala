package virtualfs.files

abstract class DirEntry(val parentPath: String, val name: String) {

    def path = parentPath + Directory.SEPARATOR + name
    def asDirectory: Directory
    def asFile: File
    def getType: String
}