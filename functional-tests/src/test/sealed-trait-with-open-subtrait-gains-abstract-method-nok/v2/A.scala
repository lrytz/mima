package foo

sealed trait T {
  def a: Int
  def b: Int
}

trait Open extends T

class C extends Open {
  def a = 1
  def b = 0
}

object Lib {
  def doIt(t: T) = t.a + t.b
}
