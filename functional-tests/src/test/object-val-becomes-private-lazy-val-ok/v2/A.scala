package foo

object Lib {
  private lazy val _it = compute
  def it = _it
  def compute = 42
}
