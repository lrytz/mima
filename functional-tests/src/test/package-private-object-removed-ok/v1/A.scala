package foo

private[foo] object O {
  def bar(x: Int) = x
}

object Lib {
  def doIt = O.bar(1)
}
