class Base[T](val value: T)

// U's bound is a type variable, so the class signature refers to T as `TT;`
class Bounded[T, U <: T](val value: U)
class BoundedParent[T, U <: T](v: U) extends Base[U](v)
