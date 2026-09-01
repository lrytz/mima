package com.typesafe.tools.mima.lib

import java.io.File

import com.typesafe.tools.mima.core._

/** The private[AccessibilityFixture] mark on Hidden is a Scala notion: it lives in the
 *  pickle of the enclosing object, while Hidden's own classfile is ACC_PUBLIC. Asking
 *  Hidden on its own is only answered correctly if reading it loads the enclosing object. */
object AccessibilityFixture {
  private[AccessibilityFixture] class Hidden
  class Shown
}

class NestedAccessibilitySpec extends munit.FunSuite {
  private val fixture = "com.typesafe.tools.mima.lib.AccessibilityFixture"

  /** A class from a tree nothing else has touched, so no pickle has been read yet. */
  private def freshly(name: String): ClassInfo = {
    val dir = new File(getClass.getProtectionDomain.getCodeSource.getLocation.toURI)
    val cp  = ClassPath.fromJarOrDir(dir).getOrElse(fail(s"not a directory: $dir"))
    new Definitions(ClassPath.of(List(cp, ClassPath.base))).fromName(name)
  }

  test("a qualified private nested class is not externally accessible, asked on its own") {
    assert(!freshly(s"$fixture$$Hidden").isExternallyAccessible)
  }

  test("a public nested class is externally accessible, asked on its own") {
    assert(freshly(s"$fixture$$Shown").isExternallyAccessible)
  }
}
