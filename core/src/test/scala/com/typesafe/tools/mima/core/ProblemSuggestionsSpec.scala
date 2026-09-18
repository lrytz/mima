package com.typesafe.tools.mima.core

final class ProblemSuggestionsSpec extends munit.FunSuite {
  private def cls(name: String)             = new SyntheticClassInfo(NoPackageInfo, name)
  private def missing(name: String)         = MissingClassProblem(cls(name))
  private def noLongerChecked(name: String) = ClassNoLongerCheckedProblem(cls(name), cls(name))

  test("a problem to accept suggests a filter, and names the file of its direction") {
    assertEquals(
      ProblemSuggestions.lines(Seq(missing("foo.C")), Nil),
      Seq(
        "To accept the incompatible changes above, add the lines below to mimaBinaryIssueFilters, or to src/main/mima-filters/<version>.backwards.excludes.",
        """   ProblemFilters.exclude[MissingClassProblem]("foo.C"),""",
      ),
    )
  }

  test("a forward problem names the forwards file") {
    assert(ProblemSuggestions.lines(Nil, Seq(missing("foo.C"))).head.endsWith("<version>.forwards.excludes."))
  }

  test("problems in both directions name both files") {
    val line = ProblemSuggestions.lines(Seq(missing("foo.C")), Seq(missing("foo.D"))).head
    assert(line.contains(".backwards.excludes or src/main/mima-filters/<version>.forwards.excludes."), line)
  }

  test("a problem that stops a definition being checked suggests a keep entry") {
    assertEquals(
      ProblemSuggestions.lines(Seq(noLongerChecked("foo.C")), Nil).last,
      """   BinaryApi.keep[ClassNoLongerCheckedProblem]("foo.C"),""",
    )
  }

  test("the same line is suggested once") {
    val lines = ProblemSuggestions.lines(Seq(missing("foo.C"), missing("foo.C")), Nil)
    assertEquals(lines.count(_.contains("""exclude[MissingClassProblem]("foo.C")""")), 1)
  }

  test("nothing is suggested for no problems")(assertEquals(ProblemSuggestions.lines(Nil, Nil), Seq.empty[String]))
}
