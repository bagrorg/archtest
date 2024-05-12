package com.github.mmvpm
package matching

trait Matchable[A, B] {
  def matches(a: A, b: B): Boolean
}

object Matchable {

  implicit class Syntax[A, B](a: A) {
    def matches(b: B)(implicit ev: Matchable[A, B]): Boolean = ev.matches(a, b)
  }
}
