package com.github.mmvpm.core
package matching

trait Matchable[A, B] {
  def matches(a: A, b: B): Boolean
}

object Matchable {

  type Matchable1[A] = Matchable[A, A]

  // default

  def byEquals[A]: Matchable[A, A] =
    (a: A, b: A) => a == b

  // option

  def optionL[A, B](m: Matchable[A, B], ifNone: Boolean = false): Matchable[Option[A], B] = {
    case (Some(a), b) => m.matches(a, b)
    case _ => ifNone
  }

  def optionR[A, B](m: Matchable[A, B], ifNone: Boolean = false): Matchable[A, Option[B]] = {
    case (a, Some(b)) => m.matches(a, b)
    case _ => ifNone
  }

  def options[A, B](
      m: Matchable[A, B],
      ifNone: Boolean = false,
      ifNones: Boolean = false): Matchable[Option[A], Option[B]] = {
    case (Some(a), Some(b)) => m.matches(a, b)
    case (None, None) => ifNones
    case _ => ifNone
  }

  // sequence

  def existsAnyPair[A, B](m: Matchable[A, B]): Matchable[Seq[A], Seq[B]] =
    (seqA: Seq[A], seqB: Seq[B]) => seqA.exists(a => seqB.exists(b => m.matches(a, b)))

  // lift

  def lift[A, B, C, D](m: Matchable[A, B])(ca: C => A, db: D => B): Matchable[C, D] =
    (c: C, d: D) => m.matches(ca(c), db(d))

  def lift1[A, C](m: Matchable[A, A])(ca: C => A): Matchable[C, C] =
    lift[A, A, C, C](m)(ca, ca)

  def liftL[A, B, C](m: Matchable[A, B])(ca: C => A): Matchable[C, B] =
    (c: C, b: B) => m.matches(ca(c), b)

  def liftR[A, B, D](m: Matchable[A, B])(db: D => B): Matchable[A, D] =
    (a: A, d: D) => m.matches(a, db(d))

  // composition

  def any[A, B](inners: Matchable[A, B]*): Matchable[A, B] =
    (a: A, b: B) => inners.exists(_.matches(a, b))

  def all[A, B](inners: Matchable[A, B]*): Matchable[A, B] =
    (a: A, b: B) => inners.forall(_.matches(a, b))

  // syntax

  implicit class Syntax[A, B](m: Matchable[A, B]) {

    def optionL: Matchable[Option[A], B] = Matchable.optionL(m)
    def optionR: Matchable[A, Option[B]] = Matchable.optionR(m)
    def options: Matchable[Option[A], Option[B]] = Matchable.options(m)

    def optionL(ifNone: Boolean): Matchable[Option[A], B] = Matchable.optionL(m, ifNone)
    def optionR(ifNone: Boolean): Matchable[A, Option[B]] = Matchable.optionR(m, ifNone)
    def options(ifNone: Boolean, ifNones: Boolean): Matchable[Option[A], Option[B]] =
      Matchable.options(m, ifNone, ifNones)

    def liftL[C](ca: C => A): Matchable[C, B] = Matchable.liftL(m)(ca)
    def liftR[D](db: D => B): Matchable[A, D] = Matchable.liftR(m)(db)
    def lift[C, D](ca: C => A, db: D => B): Matchable[C, D] = Matchable.lift(m)(ca, db)

    def or(another: Matchable[A, B]): Matchable[A, B] = Matchable.any(m, another)
    def and(another: Matchable[A, B]): Matchable[A, B] = Matchable.all(m, another)
  }

  implicit class Syntax1[A](m: Matchable[A, A]) {
    def lift1[C](ca: C => A): Matchable[C, C] = Matchable.lift1(m)(ca)
  }
}
