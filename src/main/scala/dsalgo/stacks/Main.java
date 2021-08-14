package dsalgo.stacks;

import java.io.*;
import java.util.*;
public class Main {

    public static void checkPar(String str) {

        Stack<Character> st = new Stack();
        for(int i=0;i<str.length();i++) {

            char ch = str.charAt(i);

            if(ch == ')' || ch == ']' || ch == '}') {
                if(st.isEmpty()) {
                    st.push(ch);
                     break;
                } else {
                    if(!check(st.pop(), ch)) {
                         break;
                    }
                }

            } else {
                if(ch == '(' || ch == '[' || ch == '{')
                  st.push(ch);
            }

        }

        if(st.isEmpty()) {
            System.out.println(true);
        } else {
            System.out.println(false);
        }

    }
    public static void main(String[] args) throws Exception {
        //String str = "[(a+b)+{(c+d)*(e/f)}";
        String str = "[(a + b) + {(c + d) * (e / f)}] ";
       // String str = "[(a+b)+{(c+d)*(e/f)]}";
        checkPar(str);
    }

    public static boolean check(char c1, char c2) {
        if(c1 == '(' && c2 == ')') return true;
        else if (c1 =='[' && c2 == ']') return true;
        else if (c1 == '{' && c2 == '}') return true;
        else return false;
    }

}
