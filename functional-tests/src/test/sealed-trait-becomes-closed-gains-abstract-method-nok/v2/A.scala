package foo

sealed trait T {
  def a: Int
  def b: Int
}

sealed abstract class Open extends T

final class Impl extends Open {
  def a = 1
  def b = 2
}

object Lib {
  def doIt(t: T) = t.a + t.b
}
