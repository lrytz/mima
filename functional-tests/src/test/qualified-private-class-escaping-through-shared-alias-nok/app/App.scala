object App {
  def main(args: Array[String]): Unit = {
    println(foo.Lib.makeK.asInstanceOf[foo.Lib.K].head.bar(1))
    println(foo.Lib.makeL.asInstanceOf[foo.Lib.L].bar(1))
  }
}
