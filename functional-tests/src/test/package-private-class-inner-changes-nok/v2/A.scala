package foo

private[foo] class Outer {
  class Inner {
    def bar(x: Int, y: Int) = x + y
  }
}

object Lib {
  private val outer = new Outer
  def doIt = new outer.Inner().bar(1, 0)
}
