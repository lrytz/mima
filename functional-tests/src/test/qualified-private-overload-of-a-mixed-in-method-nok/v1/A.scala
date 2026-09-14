package foo
trait T { def f(x: Int): Int = x }
class C extends T {
  private[foo] def f(s: String): Int = s.length
  def g: Int = f("ab")
}
