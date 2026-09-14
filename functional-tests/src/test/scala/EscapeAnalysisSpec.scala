package com.typesafe.tools.mima.lib

import java.nio.file.{ Files, Path, StandardCopyOption }

import com.typesafe.tools.mima.core._

import scala.collection.JavaConverters._

final class EscapeAnalysisTarget {
  def dependency: EscapeAnalysisDependency = null
  def hidden: EscapeAnalysisHidden         = null
}

final class EscapeAnalysisDependency

private[lib] final class EscapeAnalysisHidden

final class EscapeAnalysisSpec extends munit.FunSuite {
  test("the target root package does not include classpath classes") {
    val root       = Files.createTempDirectory("mima-target-root")
    val targetRoot = Files.createDirectory(root.resolve("target"))
    val classpath  = Files.createDirectory(root.resolve("classpath"))

    try {
      writeInvalidClass("Inside", targetRoot)
      writeInvalidClass("Outside", classpath)

      val targetCp = ClassPath.fromJarOrDir(targetRoot.toFile).get
      val fullCp   = ClassPath.fromJarOrDir(classpath.toFile).get
      val defs     = new Definitions(ClassPath.of(List(targetCp, fullCp, ClassPath.base)))
      val target   = new DefinitionsTargetPackageInfo(defs.root, targetCp)

      assertEquals(target.classes.keySet, Set("Inside"))
    } finally deleteTree(root)
  }

  test("escape analysis stops at the edge of the compared artifact") {
    val root       = Files.createTempDirectory("mima-escape-analysis")
    val targetRoot = Files.createDirectory(root.resolve("target"))
    val classpath  = Files.createDirectory(root.resolve("classpath"))

    try {
      copyClass(classOf[EscapeAnalysisTarget], targetRoot)
      copyClass(classOf[EscapeAnalysisHidden], targetRoot)
      // Unparseable, so that following it out of the artifact fails the test rather than slowing it down.
      writeInvalidClass(classOf[EscapeAnalysisDependency], classpath)

      // The qualified-private class escapes, the one outside the artifact is never looked at.
      val escaped = targetPackage(targetRoot, classpath).escapedClasses
      assertEquals(escaped.map(_.fullName), Set(classOf[EscapeAnalysisHidden].getName))
    } finally deleteTree(root)
  }

  private def targetPackage(targetRoot: Path, classpath: Path): PackageInfo = {
    val targetCp = ClassPath.fromJarOrDir(targetRoot.toFile).get
    val fullCp   = ClassPath.fromJarOrDir(classpath.toFile).get
    val defs     = new Definitions(ClassPath.of(List(targetCp, fullCp, ClassPath.base)))
    val pkg      = new DefinitionsTargetPackageInfo(defs.root, targetCp)
    for (pkgName <- targetCp.packages(ClassPath.RootPackage))
      pkg.packages(pkgName) = new ConcretePackageInfo(pkg, targetCp, pkgName, defs)
    pkg
  }

  private def copyClass(clazz: Class[_], root: Path): Unit = {
    val relative = classFile(clazz)
    copyResource(relative, root, required = true)
    copyResource(relative.stripSuffix(".class") + ".tasty", root, required = false)
  }

  private def copyResource(relative: String, root: Path, required: Boolean): Unit = {
    val in = getClass.getResourceAsStream(s"/$relative")
    if (required) assert(in != null, s"classfile not found: $relative")
    if (in != null) {
      val target = root.resolve(relative)
      Files.createDirectories(target.getParent)
      try Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING)
      finally in.close()
    }
  }

  private def writeInvalidClass(clazz: Class[_], root: Path): Unit = {
    val relative = classFile(clazz)
    val target   = root.resolve(relative)
    Files.createDirectories(target.getParent)
    Files.write(target, Array[Byte](0))
  }

  private def writeInvalidClass(name: String, root: Path): Unit =
    Files.write(root.resolve(s"$name.class"), Array[Byte](0))

  private def classFile(clazz: Class[_]): String = clazz.getName.replace('.', '/') + ".class"

  private def deleteTree(root: Path): Unit = {
    val paths = Files.walk(root)
    try paths.iterator.asScala.toList.reverse.foreach(path => Files.deleteIfExists(path))
    finally paths.close()
  }
}
