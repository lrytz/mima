package foo

abstract class Base {
  private[foo] def make(x: Int, y: Int): Int = x + y
}

sealed abstract class Iface

object Iface extends Base

object Lib {
  def doIt = Iface.make(1, 0)
}
