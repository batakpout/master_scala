import java.awt.geom.Area

sealed trait BinTree[+A]

case object Leaf extends BinTree[Nothing]

case class Branch[+A](value: A, left: BinTree[A], right: BinTree[A]) extends BinTree[A]


//building a tree


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
// for complete tree : height = 2 power(depth) - 1
def buildCompleteTree[A](v: Int, d: Int): BinTree[Int] = d match {
  case 0 => Leaf
  case _ => Branch(v, buildCompleteTree(2 * v, d - 1), buildCompleteTree(2 * v + 1, d - 1))
}

val l = List(1, 2, 3, 4, 5, 6, 7, 8, 9)
val tree = buildTree(l)
size(tree)
println("---size----")
depth(tree)
println("---building a complete tree------")
buildCompleteTree(1, 3)

def equal[A](tree1: BinTree[A], tree2: BinTree[A]): Boolean = (tree1, tree2) match {
  case (Leaf, Leaf)                                         => true
  case (Branch(v1, l1, r1), Branch(v2, l2, r2)) if v1 == v2 => equal(l1, l2) && equal(r1, r2)
  case _                                                    => false
}
equal(buildCompleteTree(1, 3), buildCompleteTree(1, 3))

def flip(tree: BinTree[Int]): BinTree[Int] = tree match {
  case Leaf            => Leaf
  case Branch(v, l, r) => Branch(v, flip(r), flip(l))
}

flip(buildCompleteTree(1, 3))

def flipEqual[A](tree1: BinTree[A], tree2: BinTree[A]): Boolean = (tree1, tree2) match {
  case (Leaf, Leaf)                                         => true
  case (Branch(v1, l1, r1), Branch(v2, l2, r2)) if v1 == v2 => {
    flipEqual(l1, r2) && flipEqual(l2, r1)
  }
}

val t1 = buildCompleteTree(1, 3)
val t2 = flip(t1)
val t3 = flipEqual(t1, t2)



def preOrder[A](tree: BinTree[A]): List[A] = tree match {
  case Leaf            => Nil
  case Branch(v, l, r) => v :: (preOrder(l) ++ preOrder(r))
}
val preOrderRes = preOrder(buildCompleteTree(1, 3))
def inorder[A](tree: BinTree[A]): List[A] = tree match {
  case Leaf            => Nil
  case Branch(v, l, r) => inorder(l) ++ (v :: inorder(r))
}
val inorderRes = inorder(buildCompleteTree(1, 3))

def postOrder[A](tree: BinTree[A]): List[A] = tree match {
  case Leaf            => Nil
  case Branch(v, l, r) => (postOrder(l) ++ postOrder(r)) ++ List(v)
}
val postOrderRes = postOrder(buildCompleteTree(1, 3))

println("-----tree traversal using accumulator-----")

def preOrderUsingAccumulator[A](tree: BinTree[A], acc: List[A]): List[A] = tree match {
  case Leaf => acc
  case Branch(v, l, r) => v :: preOrderUsingAccumulator(l, preOrderUsingAccumulator(r, acc))
}
val preOrderTailRes = preOrderUsingAccumulator(buildCompleteTree(1, 3), Nil)

def inOrderUsingAccumulator[A](tree: BinTree[A], acc: List[A]): List[A] = tree match {
  case Leaf => acc
  case Branch(v, l, r) => inOrderUsingAccumulator(l, v :: inOrderUsingAccumulator(r, acc))
}
val inOrderTailRes = inOrderUsingAccumulator(buildCompleteTree(1, 3), Nil)

def postOrderUsingAccumulator[A](tree: BinTree[A], acc: List[A]): List[A] = tree match {
  case Leaf => acc
  case Branch(v, l, r) => postOrderUsingAccumulator(l, postOrderUsingAccumulator(r, v :: acc))
}
val postOrderTailRes = postOrderUsingAccumulator(buildCompleteTree(1, 3), Nil)




