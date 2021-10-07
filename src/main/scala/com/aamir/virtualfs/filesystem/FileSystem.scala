package com.aamir.virtualfs.filesystem


import java.util.Scanner

import com.aamir.virtualfs.commands.Command
import com.aamir.virtualfs.files.Directory

object FileSystem extends App {

  val scanner = new Scanner(System.in)
  val root = Directory.ROOT
  var state = State(root = root, wd = root)
   while(true) {
     state.show()
     state = Command.from(scanner.nextLine()).apply(state)
   }
}
