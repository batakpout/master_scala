package dsalgo.stacks


object BalancedParenthesis extends App {


   def areParenthesisBalanced(s: String): Boolean =  {

    def rec(s: String, stk: MyStack[Char]): Boolean = {
       if(s.isEmpty) {
         if(stk.isEmpty) true else false
       } else {
         val ch = s.head
         if (ch == '{' || ch == '(' || ch == '[') {
            rec(s.tail, stk.push(ch))
         } else if (ch == '}' || ch == ')' || ch == ']') {
           if (stk.isEmpty) false else {
             if (!isMatching(stk.peek.get, ch)) false else rec(s.tail, stk.pop()._2)
           }
         } else rec(s.tail, stk)
       }
    }
    rec(s,  MyStack[Char]())
  }

  def isMatching(c1: Char, c2: Char) = (c1, c2) match {
    case ('(', ')') => true
    case ('[', ']') => true
    case ('{', '}') => true
    case _          => false
  }

  println {
    //areParenthesisBalanced("()[]{}");
    areParenthesisBalanced("[(a+b)+{(c+d)*(e/f)]}")
    //areParenthesisBalanced("[(a + b) + {(c + d) * (e / f)}]")
  }


}

object Balanced_Parenthesis_Imperative_Style extends App {

  val st = scala.collection.mutable.Stack[Char]()
  def isParenthesisBalanced(s: String): Boolean = {

    var i = 0;
    while(i < s.length()) {
      val ch = s.charAt(i)
      if(ch == '{' || ch == '(' || ch == '[') {
        st.push(ch)
      } else if(ch == '}' || ch == ')' || ch == ']') {
        if(st.isEmpty) return false;
        else {
          if(!isMatchingV2(st.pop(), ch)) return false
        }
      }
      i += 1;
    }
    if(st.isEmpty) {
      return true;
    }
    return false;
  }

  def isMatchingV2(c1: Char, c2: Char):Boolean =  {
    if(c1 == '(' && c2== ')') return true;
    else if (c1 == '[' && c2 ==  ']') return true;
    else if (c1 == '{' && c2 ==  '}') return true;
    else return false;
  }

  println {
    //isParenthesisBalanced("()[]{}");
    isParenthesisBalanced("[(a+b)+{(c+d)*(e/f)]}")
    //isParenthesisBalanced("[(a + b) + {(c + d) * (e / f)}]")
  }

}