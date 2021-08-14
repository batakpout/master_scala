package dsalgo.stacks

/**
 * Assuming brackets are already balanced
 */

object DuplicateBrackets extends App {

  def checkDuplicateBrackets(str: String) = {

    def rec(str: String, st: MyStack[Char]): Boolean = {
      if (str.isEmpty) false else {
        val ch = str.head
        if (ch == ')') {
          if (st.peek.contains('(')) true else {
            def popStack(stack: MyStack[Char]): MyStack[Char] = {
              if (!stack.peek.contains('(')) popStack(stack.pop()._2)
              else stack
            }

            rec(str.tail, popStack(st).pop()._2)
          }
        } else rec(str.tail, st.push(ch))
      }
    }

    rec(str, MyStack[Char]())
  }

  checkDuplicateBrackets("((a+b)+(c+d))")
  checkDuplicateBrackets("(a+b)+((c+d))")
  checkDuplicateBrackets("((a+b)) + (c+d)")
  checkDuplicateBrackets("((a+b))")
  checkDuplicateBrackets("(a+b)")
}