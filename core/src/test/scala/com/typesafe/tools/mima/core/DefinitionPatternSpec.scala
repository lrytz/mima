// scalafmt: { align.tokens."+" = [ { code = "," } ] }
package com.typesafe.tools.mima.core

final class DefinitionPatternSpec extends munit.FunSuite {
  // a class or a field has no signature
  check("foo.C",   "foo.C",   "", true)
  check("foo.C",   "foo.CD",  "", false)
  check("foo.C.x", "foo.C.x", "", true)

  // a method named without its signature stands for every overload
  check("foo.C.f", "foo.C.f",  "(Int)Int", true)
  check("foo.C.f", "foo.C.f",  "()Unit",   true)
  check("foo.C.f", "foo.C.fg", "(Int)Int", false)
  check("foo.C.f", "foo.C",    "",         false)

  // a method named with its signature stands for that overload alone
  check("foo.C.f(Int)Int", "foo.C.f", "(Int)Int",              true)
  check("foo.C.f(Int)Int", "foo.C.f", "(Int)Long",             false)
  check("foo.C.f(Int)Int", "foo.C.f", "(java.lang.String)Int", false)
  check("foo.C.f()Int",    "foo.C.f", "(Int)Int",              false)
  check("foo.C.f(Int)Int", "foo.C.f", "",                      false)

  check("foo.C.this(Int)Unit", "foo.C.this", "(Int)Unit", true)
  check("foo.C.this",          "foo.C.this", "(Int)Unit", true)

  // `*` stands for any part of a name, the signature included
  check("foo.*",         "foo.C.f",  "(Int)Int",      true)
  check("foo.*",         "foo.C",    "",              true)
  check("foo.C.*",       "foo.C.f",  "(Int)Int",      true)
  check("*.f",           "foo.C.f",  "(Int)Int",      true)
  check("foo.C.f(*)Int", "foo.C.f",  "(Int,Long)Int", true)
  check("foo.C.f(*)Int", "foo.C.f",  "(Int)Long",     false)
  check("foo.C.f*",      "foo.C.fg", "(Int)Int",      true)

  // the rest of a name is literal
  check("foo.C$D.f",               "foo.C$D.f", "(Int)Int",         true)
  check("foo.C.f(scala.Int[])Int", "foo.C.f",   "(scala.Int[])Int", true)
  check("foo.C.f(Int[])Int",       "foo.C.f",   "(Int)Int",         false)
  check("foo.C.f",                 "fooxC.f",   "(Int)Int",         false)

  def check(name: String, definition: String, signature: String, expected: Boolean)(
      implicit loc: munit.Location): Unit =
    test(s"$name matches $definition$signature: $expected") {
      assertEquals(new DefinitionPattern(name).matches(definition, signature), expected)
    }
}
