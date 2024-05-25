package com.github.mmvpm.parsers.shiva
package model

case class ShivaServiceConfig(common: Params, testOnly: Params, prodOnly: Params)

object ShivaServiceConfig {

  def empty: ShivaServiceConfig =
    ShivaServiceConfig(Params.empty, Params.empty, Params.empty)
}
