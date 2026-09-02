class Base[T](val value: T)

class Public extends Base[String]("hi")

// adding a parent changes the class signature but breaks nothing
trait Added
class Grows extends Base[String]("ok")
