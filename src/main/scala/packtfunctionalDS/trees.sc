sealed trait BinTree[+A]

case object Leaf extends BinTree[Nothing]

case class Branch[+A](value: A, left: BinTree[A], right: BinTree[A])
  extends BinTree[A]


//building a tree
def size = ???
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

val l = List(1, 2, 3, 4, 5, 6, 7, 8)
buildTree(l)


