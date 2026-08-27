package foo

sealed trait T {
  def a: Int
  def b: Int
}

final case class C(a: Int) extends T {
  def b = 0
}

object Lib {
  def doIt(t: T) = t.a + t.b
}
