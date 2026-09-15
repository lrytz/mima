package foo

import scala.annotation.publicInBinary

class C @publicInBinary private[foo] (x: Int) {
  @publicInBinary private[foo] def f: Int = x
  @publicInBinary private[foo] val v: Int = 2

  inline def g: Int = f + v
}

object C {
  inline def make: C = new C(1)
}
