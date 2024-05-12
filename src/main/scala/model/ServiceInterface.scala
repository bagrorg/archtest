package com.github.mmvpm
package model

import parser.model.ServiceMap.Provides

case class ServiceInterface(
    name: Option[String] = None,
    protocol: Option[String] = None,
    port: Option[Int] = None)

object ServiceInterface {

  def fromProvides(provides: Provides): ServiceInterface =
    ServiceInterface(
      name = Some(provides.name),
      protocol = Some(provides.protocol),
      port = provides.port
    )
}
