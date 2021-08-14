package dsalgo.stacks

/**
 * Assuming brackets are already balanced
 */

object DuplicateBrackets extends App {

  def checkDuplicateBrackets(str: String) = {

    def rec(str: String, st: Stack[Char]): Boolean = {
      if (str.isEmpty) false else {
        val ch = str.head
        if (ch == ')') {
          if (st.peek.contains('(')) true else {
            def popStack(stack: Stack[Char]): Stack[Char] = {
              if (!stack.peek.contains('(')) popStack(stack.pop()._2)
              else stack
            }

            rec(str.tail, popStack(st).pop()._2)
          }
        } else rec(str.tail, st.push(ch))
      }
    }

    rec(str, Stack[Char]())
  }

  checkDuplicateBrackets("((a+b)+(c+d))")
  checkDuplicateBrackets("(a+b)+((c+d))")
  checkDuplicateBrackets("((a+b)) + (c+d)")
  checkDuplicateBrackets("((a+b))")
  checkDuplicateBrackets("(a+b)")
}