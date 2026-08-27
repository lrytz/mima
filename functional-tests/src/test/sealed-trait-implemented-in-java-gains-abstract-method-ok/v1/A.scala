package foo

sealed trait T {
  def a: Int
}

final case class C(a: Int) extends T

object Lib {
  def doIt(t: T) = t.a
}
