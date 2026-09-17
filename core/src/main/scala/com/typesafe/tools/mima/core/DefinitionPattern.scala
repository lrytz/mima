package com.typesafe.tools.mima.core

import java.util.regex.Pattern

/** Matches a definition by the name a report prints for it, `foo.C.f(Int)Int` for a method.
 *
 *  `*` stands for any part of a name. A name given without a signature matches every overload.
 */
private[core] final class DefinitionPattern(name: String) {
  private def compile(text: String) = Pattern.compile(text.split("\\*", -1).map(Pattern.quote).mkString(".*"))

  private val plain     = compile(name)
  private val overloads = if (name.contains('(')) plain else compile(name + "(*")

  def matches(definition: String, signature: String): Boolean =
    if (signature.isEmpty) plain.matcher(definition).matches
    else overloads.matcher(definition + signature).matches
}
