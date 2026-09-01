package foo

private[foo] class C {
  def bar(x: Int, y: Int) = x + y
}

object Lib {
  def go: C = new C()
}
