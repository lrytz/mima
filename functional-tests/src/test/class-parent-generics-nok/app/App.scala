object App {
  def main(args: Array[String]): Unit = {
    val s: String = new Public().value
    println(s)
    val t: String = new Grows().value
    println(t)
  }
}
