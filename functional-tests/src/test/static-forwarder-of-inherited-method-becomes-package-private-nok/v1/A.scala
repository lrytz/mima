package foo

abstract class Base {
  def make(x: Int): Int = x
}

object Lib extends Base
