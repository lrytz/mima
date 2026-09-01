package foo

private[foo] class C

object Lib {
  def go: C = new C()
}
