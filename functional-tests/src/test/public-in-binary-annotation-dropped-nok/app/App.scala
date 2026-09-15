object App {
  // inlined, so the app calls f directly
  def main(args: Array[String]): Unit = println(new foo.C().g)
}
