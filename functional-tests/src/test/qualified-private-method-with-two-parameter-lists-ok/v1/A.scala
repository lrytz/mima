package foo
class C {
  private[foo] def f(a: Int)(b: Int): Int = a + b
  def g: Int = f(1)(2)
}
