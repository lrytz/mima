package foo

private[foo] class Hidden {
  def bar(x: Int) = x
}

object Lib {
  def doIt = new Hidden().bar(1)
}
