package com.typesafe.tools.mima.core

import scala.annotation.tailrec

class Signature(private val signature: String) {
  import Signature._

  /** The classes this signature names, including the ones erasure drops from a descriptor. */
  private[core] def classNames: Iterator[String] =
    classNameRe.findAllMatchIn(signature).map(_.group(1).replace('/', '.'))

  lazy val canonicalized = {
    signature.headOption match {
      // only a signature that opens with formal type parameters has any to rename;
      // a class signature such as `LBase<Ljava/lang/String;>;` has none
      case Some('<') =>
        val (formalTypeParameters, _) = FormalTypeParameter.parseList(signature.drop(1))

        val replacements = formalTypeParameters.map(_.identifier).zipWithIndex
        replacements.foldLeft(signature) { case (sig, (from, to)) =>
          sig
            .replace(s"<${from}:", s"<__${to}__:")
            .replace(s";${from}:", s";__${to}__:")
            .replace(s"T${from};", s"__${to}__;")
        }

      case _ => signature
    }
  }

  /** For a class signature, the type arguments it passes to each parent it names.
   *
   *  A client's casts come from the arguments a class passes to a parent it already had,
   *  so a parent only one version names is absent here rather than counted as a change.
   */
  private[mima] def parentTypeArgs: Map[String, String] = parentTypeArgsOf(canonicalized)

  /** As `parentTypeArgs`, keeping the type parameter names this signature declares. */
  private[mima] def rawParentTypeArgs: Map[String, String] = parentTypeArgsOf(signature)

  private def parentTypeArgsOf(sig: String): Map[String, String] = {
    val rest = if (sig.startsWith("<")) FormalTypeParameter.parseList(sig.drop(1))._2 else sig

    @tailrec def loop(in: String, acc: Map[String, String]): Map[String, String] = {
      if (in.isEmpty || in.charAt(0) != 'L') acc
      else {
        val one          = in.substring(0, endOfClassTypeSig(in))
        val cut          = one.indexOf('<')
        val (name, args) =
          if (cut == -1) (one.substring(1, one.length - 1), "")
          else (one.substring(1, cut), one.substring(cut, one.length - 1))
        loop(in.substring(one.length), acc + (name -> args))
      }
    }

    loop(rest, Map.empty)
  }

  /** The names of the type parameters this signature declares. */
  private[mima] def formalTypeParameters: List[String] =
    if (!signature.startsWith("<")) Nil
    else FormalTypeParameter.parseList(signature.drop(1))._1.map(_.identifier)

  /** This signature with each of `formals` replaced by the type argument at the same
   *  position, i.e. a parent's type parameters replaced by what a subclass passes to it.
   */
  private[mima] def substitute(formals: List[String], args: List[String]): Signature = {
    // a wildcard argument has no form that can stand in for a type variable
    val subst = formals.zip(args).collect { case (formal, arg) if "LT[".contains(arg.charAt(0)) => formal -> arg }.toMap
    if (subst.isEmpty) this
    else {
      val out = new StringBuilder
      var i   = 0
      while (i < signature.length) {
        // only where a type signature can start, so that a class named `TT` in `Lfoo/TT;`
        // does not look like a use of the variable `T`
        val startsType = i == 0 || "(<>;[)".contains(signature.charAt(i - 1))
        val semi       = if (signature.charAt(i) == 'T' && startsType) signature.indexOf(';', i) else -1
        subst.get(if (semi == -1) "" else signature.substring(i + 1, semi)) match {
          case Some(arg) => out ++= arg; i = semi + 1
          case None      => out += signature.charAt(i); i += 1
        }
      }
      Signature(out.result())
    }
  }

  def matches(newer: Signature, isConstructor: Boolean): Boolean = {
    // If the signature is identical obviously it matches
    (signature == newer.signature) ||
    // Consider missing signatures identical to non-generic ones.
    // This is particularly helpful because between Scala 3.1.1 and 3.1.2 the compiler
    // started emitting signatures for non-generic methods. Incompatibilities for those
    // will be caught through mismatching descriptors anyway.
    (signature == "" && newer.signature.indexOf('<') == -1) ||
    (newer.signature == "" && signature.indexOf('<') == -1) ||
    // Special rules for constructors
    (isConstructor && hasMatchingCtorSig(newer.signature)) ||
    // Also match when the signature only differs in the name of a type parameter
    canonicalized == newer.canonicalized
  }

  // Special case for scala#7975
  private def hasMatchingCtorSig(newer: String): Boolean =
    newer.isEmpty ||                 // ignore losing signature on constructors
      signature.endsWith(newer.tail) // ignore losing the 1st (outer) param (.tail drops the leading '(')

  // a method that takes no parameters and returns Object can have no signature
  override def toString = if (signature.isEmpty) "<missing>" else signature
}

object Signature {
  // javac's Louter<T>.Inner; form yields only the outer; a nested class is reached through innerClasses
  private val classNameRe = "L([^<>;]+)[<;]".r

  /** Where the class type signature starting at `in` ends, past its terminating ';'. */
  private def endOfClassTypeSig(in: String): Int = {
    var i     = 0
    var depth = 0
    var end   = -1
    while (end < 0 && i < in.length) {
      in.charAt(i) match {
        case '<'               => depth += 1
        case '>'               => depth -= 1
        case ';' if depth == 0 => end = i + 1
        case _                 =>
      }
      i += 1
    }
    if (end < 0) in.length else end
  }

  /** The type arguments of a `<...>`, e.g. `<Ljava/lang/String;TA;>` yields the two of them. */
  private[mima] def splitTypeArgs(in: String): List[String] = {
    if (!in.startsWith("<") || !in.endsWith(">")) Nil
    else {
      val body = in.substring(1, in.length - 1)
      val args = List.newBuilder[String]
      var i    = 0
      while (i < body.length) {
        val end = endOfTypeArg(body, i)
        if (end <= i) i = body.length // input the grammar above does not cover yields a partial parse
        else { args += body.substring(i, end); i = end }
      }
      args.result()
    }
  }

  /** Where the type argument starting at `from` ends, past its terminating ';'. */
  @tailrec private def endOfTypeArg(in: String, from: Int): Int = {
    val i = if (from < in.length && "+-".contains(in.charAt(from))) from + 1 else from
    if (i >= in.length) -1
    else in.charAt(i) match {
      case '*' => i + 1
      case '[' => endOfTypeArg(in, i + 1)
      case 'T' => in.indexOf(';', i) match { case -1 => -1; case semi => semi + 1 }
      case 'L' => i + endOfClassTypeSig(in.substring(i))
      case _   => i + 1 // a primitive, as the element type of an array
    }
  }

  def apply(signature: String): Signature = new Signature(signature)

  val none = Signature("")

  case class FormalTypeParameter(identifier: String, bound: String)

  object FormalTypeParameter {
    // running out of input ends the list, so a signature the grammar below does not
    // cover yields a partial parse rather than an exception that fails the whole run
    @tailrec def parseList(in: String, acc: List[FormalTypeParameter] = Nil): (List[FormalTypeParameter], String) = {
      if (in.isEmpty) (acc, in)
      else in(0) match {
        case '>' => (acc, in.drop(1))
        case _   => {
          val (next, rest) = parseOne(in)
          parseList(rest, acc :+ next)
        }
      }
    }

    def parseOne(in: String): (FormalTypeParameter, String) = {
      val identifier    = in.takeWhile(_ != ':')
      val boundAndRest  = in.dropWhile(_ != ':').drop(1)
      val (bound, rest) = splitBoundAndRest(boundAndRest)
      (FormalTypeParameter(identifier, bound), rest)
    }

    @tailrec private def splitBoundAndRest(in: String, boundSoFar: String = "", depth: Int = 0): (String, String) = {
      if (in.isEmpty) {
        (boundSoFar, in)
      } else if (depth > 0) {
        in(0) match {
          case '>' => splitBoundAndRest(in.drop(1), boundSoFar + '>', depth - 1)
          case '<' => splitBoundAndRest(in.drop(1), boundSoFar + '<', depth + 1)
          case o   => splitBoundAndRest(in.drop(1), boundSoFar + o, depth)
        }
      } else {
        in(0) match {
          case '<' => splitBoundAndRest(in.drop(1), boundSoFar + '<', depth + 1)
          case ';' => (boundSoFar, in.drop(1))
          case o   => splitBoundAndRest(in.drop(1), boundSoFar + o, depth)
        }
      }
    }
  }
}
