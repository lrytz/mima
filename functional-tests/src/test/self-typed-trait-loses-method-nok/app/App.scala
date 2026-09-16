object App {
  def main(args: Array[String]): Unit = {
    val t = new foo.T with foo.U { def f = 1 }
    println(t.f)
  }
}
