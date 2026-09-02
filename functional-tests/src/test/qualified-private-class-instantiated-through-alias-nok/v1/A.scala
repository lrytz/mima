package foo

private[foo] class C {
  def bar(x: Int) = x
}

object Lib {
  type K = C
}
