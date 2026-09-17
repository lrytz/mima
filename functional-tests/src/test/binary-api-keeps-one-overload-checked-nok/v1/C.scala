package foo

class C {
  private[foo] def f(i: Int): Int = i
  private[foo] def f(s: String): String = s
}

object Lib {
  def doIt = new C().f(1) + new C().f("a").length
}
