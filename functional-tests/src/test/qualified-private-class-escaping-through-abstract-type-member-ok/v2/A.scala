package foo

private[foo] class C { def bar(x: Int, y: Int) = x + y }

trait Lib {
  type K <: C
}
