package com.github.mmvpm
package matching

trait Matchable[A, B] {
  def matches(a: A, b: B): Boolean
}

object Matchable {

  type Matchable1[A] = Matchable[A, A]

  def byEquals[A]: Matchable[A, A] =
    (a: A, b: A) => a == b

  def optionL[A, B](ev: Matchable[A, B]): Matchable[Option[A], B] =
    (a: Option[A], b: B) => a.nonEmpty && ev.matches(a.get, b)

  def optionR[A, B](ev: Matchable[A, B]): Matchable[A, Option[B]] =
    (a: A, b: Option[B]) => b.nonEmpty && ev.matches(a, b.get)

  def options[A, B](ev: Matchable[A, B]): Matchable[Option[A], Option[B]] =
    (a: Option[A], b: Option[B]) => a.nonEmpty && b.nonEmpty && ev.matches(a.get, b.get)

  def lift[A, B, C, D](ev: Matchable[A, B])(ca: C => A, db: D => B): Matchable[C, D] =
    (c: C, d: D) => ev.matches(ca(c), db(d))

  def lift1[A, C](ev: Matchable[A, A])(ca: C => A): Matchable[C, C] =
    lift[A, A, C, C](ev)(ca, ca)

  def liftL[A, B, C](ev: Matchable[A, B])(ca: C => A): Matchable[C, B] =
    (c: C, b: B) => ev.matches(ca(c), b)

  def liftR[A, B, D](ev: Matchable[A, B])(db: D => B): Matchable[A, D] =
    (a: A, d: D) => ev.matches(a, db(d))

  def any[A, B](inners: Matchable[A, B]*): Matchable[A, B] =
    (a: A, b: B) => inners.exists(_.matches(a, b))

  def all[A, B](inners: Matchable[A, B]*): Matchable[A, B] =
    (a: A, b: B) => inners.forall(_.matches(a, b))

  implicit class Syntax[A, B](ev: Matchable[A, B]) {
    def optionL: Matchable[Option[A], B] = Matchable.optionL(ev)
    def optionR: Matchable[A, Option[B]] = Matchable.optionR(ev)
    def options: Matchable[Option[A], Option[B]] = Matchable.options(ev)
    def lift[C, D](ca: C => A, db: D => B): Matchable[C, D] = Matchable.lift(ev)(ca, db)
    def liftL[C](ca: C => A): Matchable[C, B] = Matchable.liftL(ev)(ca)
    def liftR[D](db: D => B): Matchable[A, D] = Matchable.liftR(ev)(db)
  }

  implicit class Syntax1[A](ev: Matchable[A, A]) {
    def lift1[C](ca: C => A): Matchable[C, C] = Matchable.lift1(ev)(ca)
  }
}
