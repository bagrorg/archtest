package com.github.mmvpm
package matching

//import io.circe.Json
//
//trait MatchableConfig[A] {
//  def matches(a: A, config: Seq[(String, Json)]): Boolean
//}
//
//object MatchableConfig {
//
//  implicit class Syntax[A: MatchableConfig](a: A) {
//
//    def matches(config: Seq[(String, Json)]): Boolean =
//      implicitly[MatchableConfig[A]].matches(a, config)
//  }
//
//  implicit def byMatchable[A: Matchable]: MatchableConfig[A] =
//    traverseWithMatchable
//
//  private def traverseWithMatchable[A: Matchable](a: A, config: Seq[(String, Json)]): Boolean =
//    config.exists { case (key, value) =>
//      value.fold(
//        jsonNull = false,
//        jsonBoolean = _ => false,
//        jsonNumber = _ => false,
//        jsonString = implicitly[Matchable[A]].matches(a, key, _),
//        jsonArray = array => traverseWithMatchable(a, array.map(key -> _)),
//        jsonObject = obj => traverseWithMatchable(a, obj.toList)
//      )
//    }
//}
