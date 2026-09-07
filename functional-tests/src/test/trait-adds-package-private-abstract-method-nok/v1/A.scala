package foo

trait T { private[foo] def m: Int }
object Lib { def call(t: T) = t.m }
