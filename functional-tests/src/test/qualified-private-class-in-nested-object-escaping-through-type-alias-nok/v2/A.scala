package foo

object Outer {
  object Inner {
    private[foo] class Deep { def bar(x: Int, y: Int) = x + y }
  }
}

object Lib {
  type K = Outer.Inner.Deep
}
