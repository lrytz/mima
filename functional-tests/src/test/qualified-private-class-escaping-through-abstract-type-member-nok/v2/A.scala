package foo

private[foo] class C { def bar(x: Int, y: Int) = x + y }

trait Lib {
  type K <: C
  def make: Any
}

// an alias inside a qualified private object leaks nothing; the bound on Lib.K does
private[foo] object LibImpl extends Lib {
  type K = C
  def make: Any = new C
}

object Libs {
  def lib: Lib = LibImpl
}
