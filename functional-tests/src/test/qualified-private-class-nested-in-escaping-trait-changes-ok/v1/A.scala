package foo
private[foo] trait Impl { private[foo] class Inner(b: Boolean) }
object Api extends Impl
