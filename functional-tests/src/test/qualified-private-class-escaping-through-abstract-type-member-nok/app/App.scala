object App {
  def main(args: Array[String]): Unit = {
    val lib = foo.Libs.lib
    val k = lib.make.asInstanceOf[lib.K]
    println(k.bar(1))
  }
}
