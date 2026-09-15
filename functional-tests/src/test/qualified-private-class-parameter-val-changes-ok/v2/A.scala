package foo
private[foo] class Node { def f(x: Int, y: Int): Int = x }
final class Box private[foo] (private[foo] val root: Node)
