package basicscala

import advancedscala.currying.PartialFunctions.chatbot

object InfiniteScanner extends App {
  scala.io.Source.stdin.getLines().foreach{ input =>
     println(s"Doing something with $input")
  }

}