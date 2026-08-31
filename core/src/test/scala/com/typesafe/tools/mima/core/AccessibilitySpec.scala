package com.typesafe.tools.mima.core

final class AccessibilitySpec extends munit.FunSuite {
  check("a public class", publicClass, true)
  check("a non-public class", clazz(flags = 0), false)
  check("a private[pkg] class", scopedPrivateClass, false)

  def check(what: String, info: ClassInfo, accessible: Boolean)(implicit loc: munit.Location): Unit =
    test(s"a client can reach $what: $accessible") {
      assertEquals(info.isExternallyAccessible, accessible)
    }

  private def clazz(flags: Int) = {
    val clazz = new SyntheticClassInfo(NoPackageInfo, "Foo")
    clazz._flags = flags
    clazz
  }

  private def publicClass = clazz(ClassfileConstants.JAVA_ACC_PUBLIC)

  private def scopedPrivateClass = {
    val clazz = publicClass
    clazz._scopedPrivate = true
    clazz
  }
}
