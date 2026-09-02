package foo

private[foo] class C { def bar(x: Int) = x }

trait Lib {
  type K <: C
}
