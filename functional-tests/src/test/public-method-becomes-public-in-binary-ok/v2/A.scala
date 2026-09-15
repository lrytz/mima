package foo

import scala.annotation.publicInBinary

class C {
  @publicInBinary private[foo] def f: Int = 1
}
