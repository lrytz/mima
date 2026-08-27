class Sub extends foo.Outer {
  class Mine extends Inner {
    def a = 1
  }
  def make: foo.T = new Mine
}

object App {
  def main(args: Array[String]): Unit = {
    println(foo.Lib.doIt(new Sub().make))
  }
}
