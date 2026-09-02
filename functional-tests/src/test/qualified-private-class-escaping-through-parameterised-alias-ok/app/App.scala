object App {
  def main(args: Array[String]): Unit = {
    println(new foo.Lib.K[Int]().bar(1))
  }
}
