package foo

private[foo] class C {
  def bar(x: Int) = x
}

object Lib {
  type K[T] = List[(T, C)]
  def make: Any = List((0, new C))
}
