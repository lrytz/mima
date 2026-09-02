package foo

private[foo] class C {
  class D {
    def bar(x: Int, y: Int) = x + y
  }
}

object Lib {
  type K = C#D
  def make: Any = { val c = new C; new c.D }
}
