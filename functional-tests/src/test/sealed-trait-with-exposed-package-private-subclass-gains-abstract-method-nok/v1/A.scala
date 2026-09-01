package foo

sealed trait T {
  def a: Int
}

private[foo] abstract class Hidden extends T

abstract class Exposed extends Hidden

object Lib {
  def doIt(t: T) = t.a
}
