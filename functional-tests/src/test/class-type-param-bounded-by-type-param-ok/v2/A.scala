class Base[T](val value: T)

class Bounded[T, U <: T](val value: U) {
  def other: U = value
}
class BoundedParent[T, U <: T](v: U) extends Base[U](v)
