package com.github.mmvpm.core
package model

final case class ServiceType(`type`: String) {
  override def toString: String = `type`
}

object ServiceType {

  val service: ServiceType =
    ServiceType("service")

  val kafka: ServiceType =
    ServiceType("kafka")

  val mysql: ServiceType =
    ServiceType("mysql")

  val postgresql: ServiceType =
    ServiceType("postgresql")

  val redis: ServiceType =
    ServiceType("redis")

  val etl: ServiceType =
    ServiceType("etl")

  val batch: ServiceType =
    ServiceType("batch")

  def from(option: Option[String]): ServiceType =
    option.map(ServiceType(_)).getOrElse(service)
}
