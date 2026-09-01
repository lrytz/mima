package foo

object Lib {
  private[foo] def hidden(x: Int, y: Int) = x + y
  def doIt = hidden(1, 0)
}
