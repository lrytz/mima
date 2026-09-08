package foo

object Holder {
  private[foo] class Inner { def bar(x: Int) = x }
  // the alias and the class it names share an owner, so the pickle refers to
  // Inner through `this`
  type K = Inner
}
