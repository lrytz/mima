package foo

sealed trait T {
  def a: Int
  def b: Int
}

abstract class Open extends T

object Lib {
  def doIt(t: T) = t.a + t.b
}
