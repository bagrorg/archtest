package com.github.mmvpm
package trash.instances

//import matching.Matchable
//
//object CommonInstances {
//
//  implicit def matchableOptionLeft[A, B](implicit ev: Matchable[A, B]): Matchable[Option[A], B] =
//    (a: Option[A], b: B) => a.nonEmpty && ev.matches(a.get, b)
//
//  implicit def matchableOptionRight[A, B](implicit ev: Matchable[A, B]): Matchable[A, Option[B]] =
//    (a: A, b: Option[B]) => b.nonEmpty && ev.matches(a, b.get)
//
//  implicit def matchableOptions[A, B](implicit ev: Matchable[A, B]): Matchable[Option[A], Option[B]] =
//    (a: Option[A], b: Option[B]) => a.nonEmpty && b.nonEmpty && ev.matches(a.get, b.get)
//}
