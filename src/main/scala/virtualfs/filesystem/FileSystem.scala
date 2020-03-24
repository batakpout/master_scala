package virtualfs.filesystem

import java.util.Scanner

import virtualfs.commands.Command
import virtualfs.files.Directory

object FileSystem extends App {

  val scanner = new Scanner(System.in)
  val root = Directory.ROOT
  var state = State(root = root, wd = root)
   while(true) {
     state.show
     state = Command.from(scanner.nextLine()).apply(state)
   }
}
