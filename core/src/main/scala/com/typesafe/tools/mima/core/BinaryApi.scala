package com.typesafe.tools.mima.core

import java.util.regex.Pattern

import scala.collection.mutable
import scala.reflect.{ ClassTag, classTag }

/** A definition mima keeps checking though the source no longer makes it part of the API. */
final class BinaryApiEntry private[core] (private[core] val problem: Class[_], val name: String) {
  private[core] val pattern = Pattern.compile(name.split("\\*", -1).map(Pattern.quote).mkString(".*"))

  private[core] def keeps(problem: Class[_], definition: String): Boolean =
    this.problem == problem && pattern.matcher(definition).matches

  override def toString = s"""BinaryApi.keep[${problem.getSimpleName}]("$name")"""
}

object BinaryApi {
  /** Keeps mima checking the named definition, answering the problem `P` it reported about it.
   *  The name is the one the problem prints, and `*` stands for any part of it. */
  def keep[P <: StopsCheckingProblem: ClassTag](name: String): BinaryApiEntry =
    new BinaryApiEntry(classTag[P].runtimeClass, name)

  private val Entry = """BinaryApi\.keep\[([^\]]+)\]\("([^"]+)"\)""".r

  /** One line of a `binary-api` file. */
  def parse(text: String): BinaryApiEntry = text.trim.stripSuffix(",") match {
    case Entry(problemName, name) => keep(problemName, name)
    case other                    => throw new IllegalArgumentException(s"not a binary-api entry: '$other'")
  }

  /** As `keep`, naming the problem as a `binary-api` file does.
   *
   *  @throws java.lang.ClassNotFoundException if there is no such problem
   *  @throws java.lang.IllegalArgumentException if the problem is not one that stops mima checking
   */
  def keep(problemName: String, name: String): BinaryApiEntry = {
    val problem = Class.forName(s"com.typesafe.tools.mima.core.$problemName")
    require(
      classOf[StopsCheckingProblem].isAssignableFrom(problem),
      s"$problemName does not stop mima checking a definition")
    new BinaryApiEntry(problem, name)
  }
}

/** The entries of one run, and which of them a definition actually needed. */
private[mima] final class BinaryApiSpec(val entries: Seq[BinaryApiEntry]) {
  private val used = mutable.Set.empty[BinaryApiEntry]

  private def keeps(problem: Class[_], definition: String) =
    entries.exists(e => e.keeps(problem, definition) && { used += e; true })

  def keepsClass(definition: String): Boolean      = keeps(classOf[ClassBecomesUnreachableProblem], definition)
  def keepsMethod(definition: String): Boolean     = keeps(classOf[MethodBecomesUnreachableProblem], definition)
  def keepsExtensible(definition: String): Boolean = keeps(classOf[HierarchyBecomesClosedProblem], definition)

  /** Entries no definition needed: a typo, or the definition is public again, or gone. */
  def unused: Seq[BinaryApiEntry] = entries.filterNot(used.contains)
}

private[mima] object BinaryApiSpec {
  val empty = new BinaryApiSpec(Nil)
}
