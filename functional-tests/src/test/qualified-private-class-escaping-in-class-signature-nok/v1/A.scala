package foo

class Base[T](val value: T)

private[foo] class C {
  def bar(x: Int) = x
}

class P extends Base[C](new C)
