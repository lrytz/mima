class D extends foo.I { def m = 2 }                    // implements what v1 declares abstract
class E extends foo.C { override def m = super.m + 1 } // invokespecial on the method that moved

object App {
  def main(args: Array[String]): Unit = {
    println(new foo.C().m)
    println((new foo.C(): foo.I).m)
    println(new D().m)
    println(new E().m)
  }
}
