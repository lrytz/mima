package foo

private[foo] class C {
  private[foo] def bar = 1
}

object Lib {
  def go: C = new C
  def use = go.bar
}
