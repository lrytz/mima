package foo

private[foo] class C { def bar(x: Int, y: Int) = x + y }

private[foo] class D { def bar(x: Int, y: Int) = x + y }

private[foo] class E { def bar(x: Int, y: Int) = x + y }

object Lib {
  type U = C | Int
  type I = D & Serializable
  type L = ([X] =>> List[X])[E]
}
