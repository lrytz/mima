package foo

abstract class Base {
  private[foo] def make(x: Long): Int = x.toInt
}

object Iface extends Base

object Lib {
  def doIt = Iface.make(1)
}
