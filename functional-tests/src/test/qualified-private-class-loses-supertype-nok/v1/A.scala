package foo

class Base {
  def base = 1
}

private[foo] class C extends Base

object Lib {
  def go: C = new C
}
