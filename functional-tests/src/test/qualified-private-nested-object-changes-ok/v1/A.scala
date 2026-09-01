package foo

object Lib {
  private[foo] object Hidden {
    def hidden(x: Int) = x
  }
  def doIt = Hidden.hidden(1)
}
