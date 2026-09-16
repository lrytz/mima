package foo

trait U

private[foo] trait T { self: U =>
  def bar(x: Int) = x
}

object Lib {
  def doIt = new U with T {}.bar(1)
}
