package foo

sealed trait T {
  def a: Int
  def b: Int = 0
}

final case class C(a: Int) extends T

object Lib {
  def doIt(t: T) = t.a + t.b
}
