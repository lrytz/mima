package com.typesafe.tools.mima.core

/** The lines a report prints under the problems: what to add to a build to accept them.
 *
 *  The sbt plugin and the CLI both print these, so that a line reads the same wherever it comes
 *  from. Each list comes last in its block, so that it is easy to copy.
 */
private[mima] object ProblemSuggestions {
  def lines(backward: Seq[Problem], forward: Seq[Problem]): Seq[String] = {
    val (keepable, filterable) = (backward ++ forward).map(p => p -> p.howToKeep).partition(_._2.isDefined)
    val (toKeep, toFilter)     = (keepable.map(_._1), filterable.map(_._1))
    // a class and its companion object share a name, so their problems can give the same line
    val keeps   = keepable.flatMap(_._2).distinct
    val filters = toFilter.flatMap(_.howToFilter).distinct

    val keepBlock =
      if (keeps.isEmpty) Nil
      else
        Seq(
          "Problems above that say a later change will no longer be reported do not break clients today.",
          "To keep mima checking those definitions after mimaPreviousArtifacts is updated, add the lines below to mimaBinaryApi, or to src/main/mima-filters/binary-api.",
          "See https://github.com/scala-garden/mima#keeping-a-definition-checked",
        ) ++ signatureNote(toKeep, "keeps") ++ keeps.map("   " + _ + ",")

    val filterBlock =
      if (filters.isEmpty) Nil
      else {
        val files = Seq("backwards" -> backward, "forwards" -> forward)
          .collect {
            case (direction, problems) if problems.exists(toFilter.contains) =>
              s"src/main/mima-filters/<version>.$direction.excludes"
          }
        Seq(
          s"To accept the incompatible changes above, add the lines below to mimaBinaryIssueFilters, or to ${files.mkString(" or ")}.",
        ) ++ signatureNote(toFilter, "filters") ++ filters.map("   " + _ + ",")
      }

    keepBlock ++ filterBlock
  }

  private def signatureNote(problems: Seq[Problem], verb: String) =
    if (problems.exists(_.matchSignature.nonEmpty))
      Seq(s"Leaving out a signature (paramTypes)resultType $verb every overload of that name.")
    else Nil
}
