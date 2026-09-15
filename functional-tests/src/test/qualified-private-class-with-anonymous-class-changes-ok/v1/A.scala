package foo

trait Conn { def addr: String }

private[foo] class Handler(prefix: String) {
  def conn(raw: String): Conn = new Conn { def addr = prefix + raw }
  def helper(x: Int): Int = x
}
