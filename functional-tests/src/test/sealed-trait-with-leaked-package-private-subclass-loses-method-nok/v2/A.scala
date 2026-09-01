package foo

sealed trait T {
  def a: Int
}

private[foo] class Open extends T {
  def a = 1
}

object Lib {
  def open: Open = new Open
}
