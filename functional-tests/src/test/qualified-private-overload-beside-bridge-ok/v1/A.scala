package foo
abstract class B[A] { def f(a: A): Int }
class C extends B[String] {
  private[foo] def f(a: Int): Int = 1
  def f(a: String): Int = 2
  def g = f(1)
}
