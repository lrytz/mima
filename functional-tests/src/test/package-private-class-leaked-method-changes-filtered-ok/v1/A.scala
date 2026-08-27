package foo

private[foo] class C {
  def bar(x: Int) = x
}

object Lib {
  def go: C = new C()
}
