package foo

object Outer {
  object Inner {
    private[foo] class Deep { def bar(x: Int) = x }
  }
}

object Lib {
  type K = Outer.Inner.Deep
}
