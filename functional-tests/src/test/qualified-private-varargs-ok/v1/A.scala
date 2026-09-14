package foo
class C {
  @scala.annotation.varargs private[foo] def f(xs: Int*): Int = xs.sum
  def g: Int = f(1, 2)
}
