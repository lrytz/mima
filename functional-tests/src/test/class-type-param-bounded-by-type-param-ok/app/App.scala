object App {
  def main(args: Array[String]): Unit = {
    val s: String = new Bounded[Any, String]("hi").value
    println(s)
    val t: String = new BoundedParent[Any, String]("ok").value
    println(t)
  }
}
