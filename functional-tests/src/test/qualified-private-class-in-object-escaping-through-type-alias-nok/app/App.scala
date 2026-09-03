object App {
  def main(args: Array[String]): Unit = {
    println(new foo.Lib.K().bar(1))
    println(new foo.OtherFile.L().bar(1))
  }
}
