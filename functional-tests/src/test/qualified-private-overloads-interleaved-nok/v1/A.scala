package foo
class C {
  def z = 0
  private[foo] def f(a: Int): Int = 1
  def y = 0
  def f(a: String): Int = 2
  private[foo] def f(a: Int, b: Int): Int = 3
  def f(a: Long): Int = 4
  def g = f(1) + f(1, 2)
}
