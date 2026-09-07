package foo

package x {
  trait T {
    def somethingSoThatTHasInitMethod = 0
    private[foo] def bar(x: Int) = x
  }
}

package y {
  // C does not override bar, so its `bar` is only ever the mixin forwarder
  class C extends x.T {
    def f = bar(0)
  }
}
