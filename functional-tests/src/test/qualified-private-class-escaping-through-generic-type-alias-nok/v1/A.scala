package foo

private[foo] class C {
  def bar(x: Int) = x
}

object Lib {
  type K = List[C]
  def make: Any = List(new C)
}
