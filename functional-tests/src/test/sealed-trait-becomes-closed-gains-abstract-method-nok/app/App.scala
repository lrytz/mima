class Mine extends foo.Open {
  def a = 1
}

object App {
  def main(args: Array[String]): Unit = {
    println(foo.Lib.doIt(new Mine))
  }
}
