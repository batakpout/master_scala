package virtualfs.commands

import virtualfs.filesystem.State

trait Command {
  def apply(state: State): State
}

object Command {
  val MKDIR = "mkdir"
  val LS = "ls"
  val PWD = "pwd"

  def emptyCommand: Command = new Command {
    override def apply(state: State): State = state
  }

  def incompleteCommand(commandName: String): Command = new Command {
    override def apply(state: State): State = state.setMessage(s"$commandName : incomplete command")
  }

  def from(input: String): Command = {
    val tokens = input.split(" ")
    if (input.isEmpty || tokens.isEmpty) emptyCommand
    else if (MKDIR.equals(tokens(0))) {
      if (tokens.length < 2) incompleteCommand(MKDIR)
      else new Mkdir(tokens(1))
    } else if(LS.equals(tokens(0))) new LS
    else if(PWD.equals(tokens(0))) new PWD
    else new UnknownCommand
  }
}