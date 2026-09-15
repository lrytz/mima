package foo
abstract class B[A] { private[foo] def f(a: A): Int = 0 }
class C extends B[String] { def g: Int = f("x") }
