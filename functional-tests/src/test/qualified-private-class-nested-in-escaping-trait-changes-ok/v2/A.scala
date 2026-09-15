package foo
private[foo] trait Impl { private[foo] class Inner(b: Boolean, s: String) }
object Api extends Impl
