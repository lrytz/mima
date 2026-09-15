package foo
class Outer {
  class Inner private[foo] (x: String) { def g: Int = x.length }
  def make: Inner = new Inner("a")
}
