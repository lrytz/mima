package foo

private[foo] class C {
  class D
}

object Lib {
  def go: C = new C()
}
