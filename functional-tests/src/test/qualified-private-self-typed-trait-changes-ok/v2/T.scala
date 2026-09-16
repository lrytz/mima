package foo

trait U

private[foo] trait T { self: U =>
  def bar(x: Int, y: Int) = x + y
}

object Lib {
  def doIt = new U with T {}.bar(1, 0)
}
