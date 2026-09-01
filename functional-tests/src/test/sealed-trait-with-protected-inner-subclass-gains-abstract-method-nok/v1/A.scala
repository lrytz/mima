package foo

sealed trait T {
  def a: Int
}

class Outer {
  protected abstract class Inner extends T
}

final case class C(a: Int) extends T

object Lib {
  def doIt(t: T) = t.a
}
