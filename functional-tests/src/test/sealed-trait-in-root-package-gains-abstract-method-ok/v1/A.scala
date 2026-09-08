// no package: TASTy calls the root package `<empty>`, so the pickle was not read at all
sealed trait T { def a: Int }
final class C extends T { def a = 1 }
