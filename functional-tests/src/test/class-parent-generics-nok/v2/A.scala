class Base[T](val value: T)

class Public extends Base[Integer](Integer.valueOf(1))

trait Added
class Grows extends Base[String]("ok") with Added
