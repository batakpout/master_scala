import java.awt.geom.Area

sealed trait BinTree[+A]

case object Leaf extends BinTree[Nothing]

case class Branch[+A](value: A, left: BinTree[A], right: BinTree[A]) extends BinTree[A]


//building a tree
def depth = ???
def buildCompleteBinaryTree[A]: BinTree[A] = ???
def equal[A](tree1: BinTree[A], tree2: BinTree[A]): Boolean = ???
def flip[A](tree: BinTree[A]): BinTree[A] = ???
def flipEqual[A](tree1: BinTree[A], tree2: BinTree[A]): Boolean = ???
def preOrder[A](tree: BinTree[A]): BinTree[A] = ???
def postOrder[A](tree: BinTree[A]): BinTree[A] = ???
def inOrder[A](tree: BinTree[A]): BinTree[A] = ???


def buildTree[A](list: List[A]): BinTree[A] = list match {
  case Nil       => Leaf
  case h :: tail => {
    val k = tail.length / 2
    Branch(h, buildTree(tail.take(k)), buildTree(tail.drop(k)))
  }
}

def size[A](tree: BinTree[A]): Int = tree match {
  case Leaf            => 0
  case Branch(_, l, r) => 1 + size(l) + size(r)
}


def depth[A](tree: BinTree[A]): Int = tree match {
  case Leaf            => 0
  case Branch(_, l, r) => 1 + (depth(l) max depth(r))
}

def buildCompleteTree[A](tree: BinTree[A], v: Int, d: Int):BinTree[A] = d match {
  case 1 => tree
  case _ => buildCompleteTree(2 * v)
}

val l = List(1, 2, 3, 4, 5, 6, 7, 8, 9)
val tree = buildTree(l)
size(tree)
println("---size----")
depth(tree)
println("---building a complete tree------")
