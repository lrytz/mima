package foo

abstract class Base {
  private[foo] def make(x: Int): Int = x
}

object Iface extends Base

object Lib {
  def doIt = Iface.make(1)
}
