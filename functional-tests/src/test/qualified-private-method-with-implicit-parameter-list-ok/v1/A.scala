package foo
class C {
  private[foo] def f(a: Int)(implicit b: Int): Int = a + b
  def g: Int = f(1)(2)
}
