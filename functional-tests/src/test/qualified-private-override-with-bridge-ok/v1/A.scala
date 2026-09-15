package foo
abstract class B[A] { private[foo] def f(a: A): Int = 0 }
class C extends B[String] {
  private[foo] override def f(a: String): Int = 1
  def g: Int = f("x")
}
