package foo

trait T {
  private[foo] def m: Int
  private[foo] def n: Int
}
object Lib { def call(t: T) = t.m + t.n }
