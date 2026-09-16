package foo

sealed trait T { def a: Int; def b: Int }
final class Impl extends T { def a = 1; def b = 2 }
