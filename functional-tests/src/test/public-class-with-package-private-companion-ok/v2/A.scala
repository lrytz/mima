package foo

class Foo {
  def bar(x: Int, y: Int) = x + y
}

private[foo] object Foo {
  def hidden(x: Int) = x
}
