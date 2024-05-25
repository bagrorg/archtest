package com.github.mmvpm.parsers.shiva

import io.circe.Json

package object model {

  type Params = Map[String, Json]

  object Params {
    def empty: Params = Map.empty
  }
}
