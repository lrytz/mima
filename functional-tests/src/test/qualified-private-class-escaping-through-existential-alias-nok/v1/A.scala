package foo

private[foo] class C { def bar(x: Int) = x }

object Lib {
  type K = List[_ <: C]
  def make: Any = List(new C)
}
