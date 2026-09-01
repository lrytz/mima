package foo

private[foo] class C {
  def bar(x: Int) = x
}

object Lib {
  def doIt = new C().bar(1)
}
