package foo

private[foo] class C {
  def bar(x: Int, y: Int) = x + y
}

object Lib {
  type K = C
  def make: Any = new C
}
