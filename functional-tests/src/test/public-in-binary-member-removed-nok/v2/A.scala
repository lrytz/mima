package foo

import scala.annotation.publicInBinary

class C @publicInBinary private[foo] (x: Long) {
  inline def g: Int = 3
}

object C {
  inline def make: C = new C(1L)
}
