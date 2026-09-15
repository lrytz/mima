package foo
class C {
  private[foo] def f(x: Int): Int = x
  val f: Int = 1
  def g = f + f(1)
}
