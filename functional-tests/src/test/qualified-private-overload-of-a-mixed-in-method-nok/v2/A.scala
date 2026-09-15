package foo
trait T { def f(x: Int): Int = x }
class C extends T {
  def g: Int = 2
}
