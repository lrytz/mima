package foo

private[foo] class Hidden {
  def bar(x: Int, y: Int) = x + y
}

object Lib {
  def doIt = new Hidden().bar(1, 0)
}
