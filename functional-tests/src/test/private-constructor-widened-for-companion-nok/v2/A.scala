package foo
class C private (x: Long) {
  private def this(s: String) = this(s.length.toLong)
}
object C { def make: C = new C(1) }
