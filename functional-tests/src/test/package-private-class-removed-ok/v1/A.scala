package foo

private[foo] class Foo {
  def bar(x: Int) = x
}

object Lib {
  def doIt = new Foo().bar(1)
}
