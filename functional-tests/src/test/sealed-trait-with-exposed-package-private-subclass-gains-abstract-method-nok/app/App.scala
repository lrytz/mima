class Mine extends foo.Exposed {
  def a = 1
}

object App {
  def main(args: Array[String]): Unit = {
    println(foo.Lib.doIt(new Mine))
  }
}
