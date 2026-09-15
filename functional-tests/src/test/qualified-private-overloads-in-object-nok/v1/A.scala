package foo
object O {
  private[foo] def f(a: Int): Int = 1
  def f(a: String): Int = 2
  private[foo] def f(a: Long): Int = 3
  def use = f(1) + f(2L)
}
