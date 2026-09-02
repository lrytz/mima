package foo

object Lib {
  private[foo] object Hidden {
    def hidden(x: Int, y: Int) = x + y
  }
  def doIt = Hidden.hidden(1, 0)
}
