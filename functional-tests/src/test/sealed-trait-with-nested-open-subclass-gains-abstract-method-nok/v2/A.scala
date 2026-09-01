package foo

sealed trait T {
  def a: Int
  def b: Int
}

sealed trait U extends T

abstract class Open extends U

object Lib {
  def doIt(t: T) = t.a + t.b
}
