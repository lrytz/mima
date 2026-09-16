package foo

// in package foo, so it can call what only foo can call
object Caller { def callF: Int = new C().f }
