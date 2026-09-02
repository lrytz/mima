package foo

private[foo] class C {
  def bar(x: Int, y: Int) = x + y
}

private[foo] class D {
  def bar(x: Int, y: Int) = x + y
}

object Lib {
  type K = List[C]
  type L = D with Serializable
}
