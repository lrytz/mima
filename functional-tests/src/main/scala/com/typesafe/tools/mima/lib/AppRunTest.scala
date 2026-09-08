// scalafmt: { align.preset = more }
package com.typesafe.tools.mima.lib

import scala.reflect.io.{ Directory, Path }
import scala.util.{ Failure, Success, Try }

/** Test running the App, using library v1 or v2. */
object AppRunTest {
  def testAppRun(testCase: TestCase, direction: Direction): Try[Unit] = {
    val (lhs, rhs) = direction.ordered(testCase.outV1, testCase.outV2) // (v1, v2); or (V2, v1) if forwards-compat
    testAppRun1(testCase, lhs, rhs, direction.oracleFile)
  }

  def testAppRun1(testCase: TestCase, v1: Directory, v2: Directory, oracleFile: Path): Try[Unit] = for {
    () <- testCase.compileBoth
    insane = testCase.versionedFile("testAppRun.insane").exists
    // `.byDesign` says the app and the oracle disagree for a reason the file states: the app is
    // not a client mima protects, or the problem it names can never be a linkage error.
    // `.pending` says mima gets this one wrong and should be fixed.
    byDesign = testCase.versionedFile("testAppRun.byDesign").exists
    pending  = testCase.versionedFile("testAppRun.pending").exists || byDesign
    oracle   = testCase.versionedFile(oracleFile)
    expectOk = testCase.blankFile(oracle)
//    () <- testCase.compileApp(v2)      // compile app with v2
//    () <- testCase.runMain(v2)         // sanity check 1: run app with v2
    () <- testCase.compileApp(v1)      // recompile app with v1
    () <- testCase.runMain(v1) match { // sanity check 2: run app with v1
      case Failure(t) if !insane => Failure(new Exception("Sanity runMain check failed", t, true, false) {})
      case _                     => Success(())
    }
    () <- testCase.runMain(v2) match { // test: run app, compiled with v1, with v2
      case Failure(t) if !pending && expectOk =>
        Failure(new Exception(s"running App failed, though ${oracle.name} lists no problems: $t", t))
      case Success(()) if !pending && !expectOk =>
        Failure(new Exception(s"expected running App to fail, since ${oracle.name} lists problems"))
      case _ => Success(())
    }
  } yield ()
}
