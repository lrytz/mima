package foo

abstract class Base {
  private[foo] def make(x: Int): Int = x
}

object Lib extends Base
