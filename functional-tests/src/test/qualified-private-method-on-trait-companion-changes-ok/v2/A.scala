package foo

sealed trait Iface

object Iface {
  private[foo] def make(x: Int, y: Int): Iface = new Iface {}
}

object Lib {
  def doIt = Iface.make(1, 0)
}
