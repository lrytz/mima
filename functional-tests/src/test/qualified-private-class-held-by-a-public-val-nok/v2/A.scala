package foo

private[foo] class C {
  def bar(x: Int, y: Int) = x + y
}

private[foo] object C {
  def mk = new C
}

// a val has a descriptor, Lfoo/C$;, so the ordinary escape search finds the
// companion and then C through its own signatures: no alias reading involved
object Lib {
  val K = C
}
