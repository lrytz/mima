package foo
class C {
  private[foo] def f(xs: Int*): Int = xs.sum
  def g: Int = f(1, 2)
}
