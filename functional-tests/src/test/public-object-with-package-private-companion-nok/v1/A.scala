package foo

private[foo] class C {
  def bar(x: Int) = x
}

object C extends C {
  def baz(x: Int) = x
}
