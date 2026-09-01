package foo

sealed trait T {
  def a: Int
  def gone: Int = 1
}

final case class C(a: Int) extends T

object Lib {
  def get: T = C(1)
}
