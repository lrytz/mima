class D extends foo.I // v2's I.m is concrete, so D gets a mixin forwarder to it

object App {
  def main(args: Array[String]): Unit = {
    println(new D().m)
  }
}
