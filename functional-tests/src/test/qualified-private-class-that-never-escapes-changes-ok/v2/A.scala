package foo

private[foo] class C {
  def bar(x: Int, y: Int) = x + y
}

object Lib {
  def doIt = new C().bar(1, 0)
}
