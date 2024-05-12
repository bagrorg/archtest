package com.github.mmvpm
package parser

import io.circe.Json

package object model {

  type Params = Map[String, Json]

  object Params {
    def empty: Params = Map.empty
  }
}
