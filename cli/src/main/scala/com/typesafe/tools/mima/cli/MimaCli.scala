package com.typesafe.tools.mima.cli

import com.typesafe.tools.mima.core.ProblemSuggestions
import com.typesafe.tools.mima.lib.MiMaLib

import java.io.File
import scala.annotation.tailrec

case class Main(
    classpath: Seq[File] = Nil,
    oldBinOpt: Option[File] = None,
    newBinOpt: Option[File] = None,
    formatter: ProblemFormatter = ProblemFormatter(),
    showSuggestions: Boolean = false
) {

  def run(): Int = {
    val oldBin = oldBinOpt.getOrElse(
      throw new IllegalArgumentException("Old binary was not specified")
    )
    val newBin = newBinOpt.getOrElse(
      throw new IllegalArgumentException("New binary was not specified")
    )
    if (!oldBin.exists())
      throw new IllegalArgumentException(s"oldfile does not exist: $oldBin")
    if (!newBin.exists())
      throw new IllegalArgumentException(s"newfile does not exist: $newBin")
    val found    = new MiMaLib(classpath).collectProblems(oldBin, newBin, Nil)
    val problems = found.filter(formatter.shows)
    problems.flatMap(formatter.formatProblem).foreach(println)
    if (showSuggestions)
      ProblemSuggestions.lines(problems, Nil).foreach(println)
    problems.size
  }

}

object Main {

  def main(args: Array[String]): Unit = System.exit(run(args))

  /** 0 when the versions are compatible, 1 when they are not, 2 when the arguments are wrong. */
  def run(args: Array[String]): Int =
    try if (parseArgs(args.toList, Main()).run() == 0) 0 else 1
    catch {
      case err: IllegalArgumentException =>
        println(err.getMessage())
        printUsage()
        2
    }

  def printUsage(): Unit = println(
    s"""Usage:
      |
      |mima [OPTIONS] oldfile newfile
      |
      |  oldfile: Old (or, previous) files - a JAR or a directory containing classfiles
      |  newfile: New (or, current) files - a JAR or a directory containing classfiles
      |
      |Options:
      |  -cp CLASSPATH:
      |     Specify Java classpath, separated by '${File.pathSeparatorChar}'
      |
      |  -v, --verbose:
      |     Show a human-readable description of each problem
      |
      |  -f, --forward-only:
      |    Show only forward-binary-compatibility problems
      |
      |  -b, --backward-only:
      |    Show only backward-binary-compatibility problems
      |
      |  -g, --include-generics:
      |    Include generic signature problems, which may not directly cause bincompat
      |    problems and are hidden by default. Has no effect if using --forward-only.
      |
      |  -j, --bytecode-names:
      |    Show bytecode names of fields and methods, rather than human-readable names
      |
      |  -s, --suggestions:
      |    Print the lines to add to a build to accept the problems
      |
      |Exit code: 0 if no problems were found, 1 if there were, 2 for a usage error.
      |""".stripMargin
  )

  @tailrec
  private def parseArgs(remaining: List[String], current: Main): Main =
    remaining match {
      case Nil                                      => current
      case ("-cp" | "--classpath") :: cpStr :: rest =>
        parseArgs(
          rest,
          current.copy(classpath =
            cpStr.split(File.pathSeparatorChar).toSeq.map(new File(_))
          )
        )

      case ("-s" | "--suggestions") :: rest =>
        parseArgs(rest, current.copy(showSuggestions = true))

      case ("-f" | "--forward-only") :: rest =>
        parseArgs(
          rest,
          current.copy(formatter =
            current.formatter.copy(showForward = true, showBackward = false)
          )
        )

      case ("-b" | "--backward-only") :: rest =>
        parseArgs(
          rest,
          current.copy(formatter =
            current.formatter.copy(showForward = false, showBackward = true)
          )
        )

      case ("-j" | "--bytecode-names") :: rest =>
        parseArgs(
          rest,
          current.copy(formatter =
            current.formatter.copy(useBytecodeNames = true)
          )
        )

      case ("-v" | "--verbose") :: rest =>
        parseArgs(
          rest,
          current.copy(formatter =
            current.formatter.copy(showDescriptions = true)
          )
        )

      case ("-g" | "--include-generics") :: rest =>
        parseArgs(
          rest,
          current.copy(formatter =
            current.formatter.copy(showIncompatibleSignature = true)
          )
        )

      case filename :: rest if current.oldBinOpt.isEmpty =>
        parseArgs(rest, current.copy(oldBinOpt = Some(new File(filename))))
      case filename :: rest if current.newBinOpt.isEmpty =>
        parseArgs(rest, current.copy(newBinOpt = Some(new File(filename))))
      case wut :: _ =>
        throw new IllegalArgumentException(s"Unknown argument $wut")
    }

}
