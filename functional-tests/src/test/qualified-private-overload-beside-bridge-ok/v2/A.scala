package foo
abstract class B[A] { def f(a: A): Int }
class C extends B[String] { def f(a: String): Int = 9; def g = 0 }
