package foo

object Lib {
  private[foo] def hidden(x: Int) = x
  def doIt = hidden(1)
}
