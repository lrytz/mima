object App {
  def main(args: Array[String]): Unit = {
    // a union has no members to call, so C is named but never dereferenced
    println(foo.Lib.makeI.asInstanceOf[foo.Lib.I].bar(1))
    println(foo.Lib.makeL.asInstanceOf[foo.Lib.L].head.bar(1))
  }
}
