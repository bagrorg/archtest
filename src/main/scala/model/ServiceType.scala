package com.github.mmvpm
package model

final case class ServiceType(`type`: String) extends AnyVal {
  override def toString: String = `type`
}

object ServiceType {

  val service: ServiceType =
    ServiceType("service")

  val kafka: ServiceType =
    ServiceType("kafka")

  def from(option: Option[String]): ServiceType =
    option.map(ServiceType(_)).getOrElse(service)
}
