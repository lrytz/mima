package foo
class C(a: Int) {
  private[foo] def this(s: String) = this(s.length)
  def this(l: Long) = this(l.toInt)
}
