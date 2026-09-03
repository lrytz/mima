package foo

object Holder {
  private[foo] class Inner { def bar(x: Int, y: Int) = x + y }
}

object Lib {
  type K = Holder.Inner
}
