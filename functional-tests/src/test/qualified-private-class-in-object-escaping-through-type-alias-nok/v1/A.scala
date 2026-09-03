package foo

object Holder {
  private[foo] class Inner { def bar(x: Int) = x }
}

object Lib {
  type K = Holder.Inner
}
