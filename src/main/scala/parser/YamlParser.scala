package com.github.mmvpm
package parser

import io.circe.{Decoder, Error}
import io.circe.yaml.Parser

import java.io.{File, FileInputStream, InputStreamReader}

class YamlParser {

  def parse[T: Decoder](yaml: File): Either[Error, T] =
    Parser.default
      .parse(new InputStreamReader(new FileInputStream(yaml)))
      .flatMap(_.as[T])

  def parseUnsafe[T: Decoder](yaml: File): T =
    parse(yaml) match {
      case Left(error) =>
        println(s"Failed to parse $yaml: $error")
        throw error
      case Right(value) =>
        value
    }

  def parseIgnoringEmpty[T: Decoder](yaml: File, ifFileEmpty: T): Either[Error, T] =
    Parser.default
      .parse(new InputStreamReader(new FileInputStream(yaml)))
      .flatMap {
        case json if json.asBoolean.contains(false) => Right(ifFileEmpty)
        case json => json.as[T]
      }
}
