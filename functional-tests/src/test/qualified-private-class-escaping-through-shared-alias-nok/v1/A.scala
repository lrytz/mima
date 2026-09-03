package foo

private[foo] class C {
  def bar(x: Int) = x
}

private[foo] class D extends Serializable {
  def bar(x: Int) = x
}

object Lib {
  type K = List[C]
  type L = D with Serializable

  def makeK: Any = List(new C)
  def makeL: Any = new D
}
