package com.github.mmvpm
package model

import parser.model.{ServiceConfig => ServiceConfigRaw, Params}

case class ServiceConfig(
    common: ServiceConfig.Layer,
    test: ServiceConfig.Layer,
    prod: ServiceConfig.Layer) {

  lazy val all: ServiceConfig.Layer = common + test + prod
  lazy val prodWithCommon: ServiceConfig.Layer = common + prod
  lazy val testWithCommon: ServiceConfig.Layer = common + test
}

object ServiceConfig {

  def empty: ServiceConfig =
    ServiceConfig(Layer.empty, Layer.empty, Layer.empty)

  def from(raw: ServiceConfigRaw): ServiceConfig =
    ServiceConfig(
      common = Layer.from(raw.common),
      test = Layer.from(raw.test),
      prod = Layer.from(raw.prod)
    )

  case class Layer(params: Seq[KeyValuePair]) {
    def +(other: Layer): Layer = Layer(params ++ other.params)
  }

  object Layer {

    def empty: Layer =
      Layer(Seq.empty)

    def from(raw: Params): Layer =
      Layer(
        raw.toSeq.collect {
          case (key, value) if value.isString =>
            KeyValuePair(key, value.asString.get)
        }
      )
  }
}
