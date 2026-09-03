package foo

private[foo] class C {
  def bar(x: Int, y: Int) = x + y
}

object Lib {
  type K = List[C]
  def make: Any = List(new C)
}
