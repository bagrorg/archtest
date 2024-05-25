package com.github.mmvpm.core
package model

case class ServiceConfig(
    common: ServiceConfig.Layer,
    testOnly: ServiceConfig.Layer,
    prodOnly: ServiceConfig.Layer) {

  lazy val all: ServiceConfig.Layer = common + testOnly + prodOnly
  lazy val prod: ServiceConfig.Layer = common + prodOnly
  lazy val test: ServiceConfig.Layer = common + testOnly
}

object ServiceConfig {

  def empty: ServiceConfig =
    ServiceConfig(Layer.empty, Layer.empty, Layer.empty)

  case class Layer(params: Seq[KeyValuePair]) {
    def +(other: Layer): Layer = Layer(params ++ other.params)
  }

  object Layer {
    def empty: Layer = Layer(Seq.empty)
  }
}
