package foo

trait Conn { def addr: String }

private[foo] class Handler(prefix: String) {
  def conn(raw: String): Conn = new Conn {
    class Part { def value = prefix + raw }
    def addr = new Part().value
  }
  def helper(x: Int): Int = x
}
