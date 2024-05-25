package com.github.mmvpm.core
package model

final case class ServiceFqn(fqn: String) {

  override def toString: String = fqn

  def split: (ServiceType, ServiceName) = {
    val (typeRaw, nameRaw) =
      fqn.split("/").takeRight(2) match {
        case Array(name) => (None, name)
        case Array(serviceType, name) => (Some(serviceType), name)
      }
    (ServiceType.from(typeRaw), ServiceName(nameRaw))
  }
}

object ServiceFqn {

  def service(name: String): ServiceFqn =
    ServiceFqn(name)

  def kafka(name: String): ServiceFqn =
    ServiceFqn("kafka/" + name)

  def mysql(name: String): ServiceFqn =
    ServiceFqn("mysql/" + name)

  def postgresql(name: String): ServiceFqn =
    ServiceFqn("postgresql/" + name)

  def redis(name: String): ServiceFqn =
    ServiceFqn("redis/" + name)

  def etl(name: String): ServiceFqn =
    ServiceFqn("etl/" + name)

  def batch(name: String): ServiceFqn =
    ServiceFqn("batch/" + name)

  def of(service: Service): Option[ServiceFqn] = {
    val prefix = service.`type` match {
      case ServiceType.service => ""
      case ServiceType(rawType) => rawType + "/"
    }
    service.name.map(prefix + _).map(ServiceFqn(_))
  }
}
