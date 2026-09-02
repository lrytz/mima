package com.typesafe.tools.mima.core

final class SignatureSpec extends munit.FunSuite {
  val promiseSig =
    "Lscala/concurrent/Promise<" +
      "Lscala/Function1<" +
      "Lscala/concurrent/duration/FiniteDuration;" +
      "Lscala/concurrent/Future<Lakka/http/scaladsl/Http$HttpTerminated;>;" +
      ">;" +
      ">;"

  val `signature_in_2.12.8` = Signature(s"(Lakka/http/impl/engine/server/GracefulTerminatorStage;$promiseSig)V")
  val `signature_in_2.12.9` = Signature(s"($promiseSig)V")

  test("The method checker should allow dropping the first parameter of the Signature attribute of a constructor") {
    // Assuming the descriptor is the same,
    // dropping the first parameter of the Signature attribute
    // can only be explained by going from a Scala version that does not have
    // the fix in scala#7975 (2.12.8, 2.13.0) to one that does
    assert(`signature_in_2.12.8`.matches(`signature_in_2.12.9`, true))
  }

  test("The method checker should reject adding the first parameter of the Signature attribute of a constructor back") {
    assert(!`signature_in_2.12.9`.matches(`signature_in_2.12.8`, true))
  }

  test("The method checker should allow renaming a generic parameter") {
    val withU = Signature("<U:Ljava/lang/Object;>(TU;Lscala/collection/immutable/List<TU;>;)Lscala/Option<TU;>;")
    val withT = Signature("<T:Ljava/lang/Object;>(TT;Lscala/collection/immutable/List<TT;>;)Lscala/Option<TT;>;")

    assert(withU.matches(withT, false))
  }

  // signatures as scalac emits them for the class, i.e. formal type parameters then parents
  test("The class signature parser should split the parents and keep their type arguments") {
    assertEquals(
      Signature("Ljava/lang/Object;LDialogSource<Ljava/lang/String;>;").parentTypeArgs,
      Map("java/lang/Object" -> "", "DialogSource" -> "<Ljava/lang/String;>"),
    )
  }

  test("The class signature parser should not confuse a nested type argument for a parent") {
    assertEquals(
      Signature("LBase<Lscala/Tuple2<Ljava/lang/String;Ljava/lang/String;>;>;").parentTypeArgs,
      Map("Base" -> "<Lscala/Tuple2<Ljava/lang/String;Ljava/lang/String;>;>"),
    )
  }

  test("The class signature parser should rename formal type parameters, so only the arguments differ") {
    val withT = Signature("<T:Ljava/lang/Object;>Ljava/lang/Object;LBase<TT;>;")
    val withA = Signature("<A:Ljava/lang/Object;>Ljava/lang/Object;LBase<TA;>;")
    assertEquals(withT.parentTypeArgs, withA.parentTypeArgs)
  }

  test("The class signature parser should yield nothing for a class with no signature") {
    assertEquals(Signature.none.parentTypeArgs, Map.empty[String, String])
  }

  // a type parameter bounded by another one, e.g. `class A[T, U <: T]`
  test("The class signature parser should keep the ';' that terminates a type variable reference") {
    assertEquals(
      Signature("<T:Ljava/lang/Object;U:TT;>Ljava/lang/Object;").canonicalized,
      "<__0__:Ljava/lang/Object;__1__:__0__;>Ljava/lang/Object;",
    )
  }

  test("The class signature parser should handle a type parameter bounded by another type parameter") {
    assertEquals(
      Signature("<T:Ljava/lang/Object;U:TT;>Ljava/lang/Object;").parentTypeArgs,
      Map("java/lang/Object" -> ""),
    )
    assertEquals(
      Signature("<Sub:Ljava/lang/Object;Semi:TSub;>Ljava/lang/Object;Lscala/collection/Stepper$EfficientSplit;")
        .parentTypeArgs,
      Map("java/lang/Object" -> "", "scala/collection/Stepper$EfficientSplit" -> ""),
    )
  }

  test("The signature parser should stop at the end of the input rather than throw") {
    import Signature.FormalTypeParameter
    assertEquals(
      FormalTypeParameter.parseList("T:Ljava/lang/Object;"),
      (List(FormalTypeParameter("T", "Ljava/lang/Object")), ""))
    assertEquals(FormalTypeParameter.parseList(""), (List.empty[FormalTypeParameter], ""))
  }

  test("The signature parser should parse a signature with generic bounds that themselves have generics") {
    import Signature.FormalTypeParameter
    val rest = "(TT;Lscala/collection/immutable/List<TU;>;)Lscala/Option<TT;>;>"
    val orig = s"T:Ljava/lang/Object;U:Lscala/collection/immutable/List<TT;>;>$rest"

    val (types, obtRest) = FormalTypeParameter.parseList(orig)
    assertEquals(types.length, 2)
    assertEquals(types(0), FormalTypeParameter("T", "Ljava/lang/Object"))
    assertEquals(types(1), FormalTypeParameter("U", "Lscala/collection/immutable/List<TT;>"))
    assertEquals(obtRest, rest)
  }
}
