package foo

private[foo] class Outer {
  class Inner {
    def bar(x: Int) = x
  }
}

object Lib {
  private val outer = new Outer
  def doIt = new outer.Inner().bar(1)
}
