package foo
trait U
trait T { self: U =>
  def f: Int
}
