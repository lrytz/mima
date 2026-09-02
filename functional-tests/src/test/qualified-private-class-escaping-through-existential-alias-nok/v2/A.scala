package foo

private[foo] class C { def bar(x: Int, y: Int) = x + y }

object Lib {
  type K = List[_ <: C]
  def make: Any = List(new C)
}
