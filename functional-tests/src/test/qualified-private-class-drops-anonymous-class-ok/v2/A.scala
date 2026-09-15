package foo

trait Conn { def addr: String }

private[foo] class Handler(prefix: String) {
  def conn(raw: String): Conn = SimpleConn(prefix + raw)
  def helper(x: Int): Int = x
}

private[foo] case class SimpleConn(addr: String) extends Conn
