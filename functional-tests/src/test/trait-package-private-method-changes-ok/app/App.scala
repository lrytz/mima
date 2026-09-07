object App {
  def main(args: Array[String]): Unit = {
    println(foo.Lib.doIt)
    println(foo.Lib.call(new bar.X))
  }
}
