package foo

sealed trait T {
  def a: Int
}

private[foo] abstract class Open extends T

final case class C(a: Int) extends T

object Lib {
  def doIt(t: T) = t.a
  def open: Open = null
}
