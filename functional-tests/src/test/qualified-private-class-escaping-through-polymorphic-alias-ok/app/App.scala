object App {
  def main(args: Array[String]): Unit = {
    val k = foo.Lib.make.asInstanceOf[foo.Lib.K[Int]]
    println(k.head._2.bar(1))
  }
}
