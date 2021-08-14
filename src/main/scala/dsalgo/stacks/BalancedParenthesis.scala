package dsalgo.stacks


object BalancedParenthesis extends App {

  def areParenthesisBalanced(inputArray: List[Char], stk: Stack[Char]): Boolean = inputArray match {
    case Nil       => if (stk.isEmpty) true else false
    case h :: tail => {
      if (h == '{' || h == '(' || h == '[') {
        areParenthesisBalanced(tail, stk.push(h))
      } else if (h == '}' || h == ')' || h == ']') {
        if (stk.isEmpty) false else {
          val (pop, stack) = stk.pop()
          if (!isMatching(pop.get, h)) false else areParenthesisBalanced(tail, stack)
        }
      } else false
    }
  }

 import scala.collection.mutable.Stack
  def areParenthesisBalancedV2(s: String): Boolean =  {

    def rec(s: String, stk: Stack[Char]): Boolean = {
       if(s.isEmpty) {
         if(stk.isEmpty) true else false
       } else {
         if (s.head == '{' || s.head == '(' || s.head == '[') {
           rec(s.tail, stk.push(s.head))
         } else if (s.head == '}' || s.head == ')' || s.head == ']') {
           if (stk.isEmpty) false else {
             if (!isMatching(stk.pop(), s.head)) false else rec(s.tail, stk)
           }
         } else false
       }
    }
    rec(s,  Stack[Char]())
  }

  def isMatching(c1: Char, c2: Char) = (c1, c2) match {
    case ('(', ')') => true
    case ('[', ']') => true
    case ('{', '}') => true
    case _          => false
  }

  println {
    //val l = List('{', '(', '[', ']', ')', '}')//true
    val l = List('(', '(', ')', '(', ')', ')')//true
    //val l = List('{', '(', '[', ')', ')', '}')//false
   // areParenthesisBalanced(l, Stack.empty)
    areParenthesisBalancedV2("()[]{}");
  }


}