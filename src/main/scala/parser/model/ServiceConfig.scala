package com.github.mmvpm
package parser.model

case class ServiceConfig(common: Params, test: Params, prod: Params)

object ServiceConfig {

  def empty: ServiceConfig =
    ServiceConfig(Params.empty, Params.empty, Params.empty)
}
