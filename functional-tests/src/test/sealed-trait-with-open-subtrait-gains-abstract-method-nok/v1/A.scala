package foo

sealed trait T {
  def a: Int
}

trait Open extends T

class C extends Open {
  def a = 1
}

object Lib {
  def doIt(t: T) = t.a
}
