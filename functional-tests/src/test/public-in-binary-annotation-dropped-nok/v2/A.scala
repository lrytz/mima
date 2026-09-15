package foo

class C {
  private[foo] def f: Int = 1

  inline def g: Int = f
}
