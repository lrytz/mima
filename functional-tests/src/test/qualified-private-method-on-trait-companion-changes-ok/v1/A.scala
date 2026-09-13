package foo

sealed trait Iface

object Iface {
  private[foo] def make(x: Int): Iface = new Iface {}
}

object Lib {
  def doIt = Iface.make(1)
}
