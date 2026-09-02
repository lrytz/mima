package com.typesafe.tools.mima.core

// Shapes as ClassfileParser stores them, from dotty's output for `package nx; object Foo { object Bar { object Baz } }`.
final class NestedObjectFieldSpec extends munit.FunSuite {
  check("the field of a nested object", "Foo$", "Bar", "Lnx.Foo$Bar$;", true)
  check("the field of an object two deep", "Foo$Bar$", "Baz", "Lnx.Foo$Bar$Baz$;", true)
  check("MODULE$", "Foo$", "MODULE$", "Lnx.Foo$;", false)
  check("a field of a nested class, not object", "Foo$", "Bar", "Lnx.Foo$Bar;", false)
  check("a field named unlike the object it holds", "Foo$", "bar", "Lnx.Foo$Bar$;", false)
  check("the same shape on a class", "Cls", "Bar", "Lnx.Cls$Bar$;", false)
  check("the same shape, not static", "Foo$", "Bar", "Lnx.Foo$Bar$;", false, static = false)

  def check(what: String, owner: String, name: String, descriptor: String, expected: Boolean, static: Boolean = true)(
      implicit loc: munit.Location
  ): Unit =
    test(s"mima skips $what: $expected") {
      assertEquals(field(owner, name, descriptor, static).isNestedObjectField, expected)
    }

  private def field(owner: String, name: String, descriptor: String, static: Boolean) = {
    val pkg   = new SyntheticPackageInfo(NoPackageInfo, "nx")
    val clazz = new SyntheticClassInfo(pkg, owner)
    val flags = ClassfileConstants.JAVA_ACC_PUBLIC | (if (static) ClassfileConstants.JAVA_ACC_STATIC else 0)
    new FieldInfo(clazz, name, flags, descriptor)
  }
}
