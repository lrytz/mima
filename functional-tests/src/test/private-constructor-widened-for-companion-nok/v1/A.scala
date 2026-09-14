package foo
class C private (x: Int) {
  private def this(s: String) = this(s.length)
}
object C { def make: C = new C(1) }
