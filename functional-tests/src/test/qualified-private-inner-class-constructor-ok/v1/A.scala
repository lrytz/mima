package foo
class Outer {
  class Inner private[foo] (x: Int) { def g: Int = x }
  def make: Inner = new Inner(1)
}
