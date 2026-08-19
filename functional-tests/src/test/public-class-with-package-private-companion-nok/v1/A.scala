package foo

class Foo {
  def bar(x: Int) = x
}

private[foo] object Foo {
  def hidden(x: Int) = x
}
