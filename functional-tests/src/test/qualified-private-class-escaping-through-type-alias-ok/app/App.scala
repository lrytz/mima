object App {
  def main(args: Array[String]): Unit = {
    val k = foo.Lib.make.asInstanceOf[foo.Lib.K]
    println(k.bar(1))
  }
}
