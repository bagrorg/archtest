package com.github.mmvpm.core
package model

import matching.Matchable

case class Service(
    name: Option[ServiceName] = None,
    `type`: ServiceType = ServiceType.service,
    groups: Seq[ServiceGroup] = Seq.empty,
    interfaces: Seq[ServiceInterface] = Seq.empty,
    host: Option[Host] = None,
    ip: Option[Ip] = None,
    dependsOn: Seq[Service] = Seq.empty,
    config: ServiceConfig = ServiceConfig.empty) {

  val fqn: Option[ServiceFqn] =
    ServiceFqn.of(service = this)

  // builder

  def withName(nameRaw: String): Service =
    this.copy(name = Some(ServiceName(nameRaw)))

  def withName(name: ServiceName): Service =
    this.copy(name = Some(name))

  def withType(typeRaw: String): Service =
    this.copy(`type` = ServiceType(typeRaw))

  def withType(`type`: ServiceType): Service =
    this.copy(`type` = `type`)

  def withFqn(fqn: String): Service = {
    val (serviceType, name) = ServiceFqn(fqn).split
    this.copy(name = Some(name), `type` = serviceType)
  }

  def withGroups(groups: ServiceGroup*): Service =
    this.copy(groups = groups)

  def withInterfaces(interfaces: ServiceInterface*): Service =
    this.copy(interfaces = interfaces)

  def withHost(hostRaw: String): Service =
    this.copy(host = Some(Host(hostRaw)))

  def withHost(host: Host): Service =
    this.copy(host = Some(host))

  def withIp(ipRaw: String): Service =
    this.copy(ip = Some(Ip(ipRaw)))

  def withIp(ip: Ip): Service =
    this.copy(ip = Some(ip))

  def withDependsOn(dependsOn: Service*): Service =
    this.copy(dependsOn = dependsOn)

  def withConfig(config: ServiceConfig): Service =
    this.copy(config = config)

  // match with raw config

  def existsInConfig(predicate: KeyValuePair => Boolean): Boolean =
    config.all.params.exists(predicate)

  def existsInConfigProd(predicate: KeyValuePair => Boolean): Boolean =
    config.prod.params.exists(predicate)

  def existsInConfigTest(predicate: KeyValuePair => Boolean): Boolean =
    config.test.params.exists(predicate)

  // match with other service

  def hasDependencyOn(
      other: Service
    )(implicit
      forServices: Matchable[Service, Service],
      forConfigs: Matchable[Service, KeyValuePair]): Boolean =
    hasDependencyInServiceMap(other)(forServices) || hasDependencyInConfig(other)(forConfigs)

  def hasDependencyInServiceMap(other: Service)(implicit m: Matchable[Service, Service]): Boolean =
    dependsOn.exists(m.matches(other, _))

  def hasDependencyInConfig(other: Service)(implicit m: Matchable[Service, KeyValuePair]): Boolean =
    config.all.params.exists(m.matches(other, _))
}

object Service {

  // builder

  def withName(nameRaw: String): Service =
    new Service(name = Some(ServiceName(nameRaw)))

  def withName(name: ServiceName): Service =
    new Service(name = Some(name))

  def withType(typeRaw: String): Service =
    new Service(`type` = ServiceType(typeRaw))

  def withType(`type`: ServiceType): Service =
    new Service(`type` = `type`)

  def withFqn(fqn: String): Service = {
    val (serviceType, name) = ServiceFqn(fqn).split
    new Service(name = Some(name), `type` = serviceType)
  }

  def withGroups(groups: ServiceGroup*): Service =
    new Service(groups = groups)

  def withInterfaces(interfaces: ServiceInterface*): Service =
    new Service(interfaces = interfaces)

  def withHost(hostRaw: String): Service =
    new Service(host = Some(Host(hostRaw)))

  def withHost(host: Host): Service =
    new Service(host = Some(host))

  def withIp(ipRaw: String): Service =
    new Service(ip = Some(Ip(ipRaw)))

  def withIp(ip: Ip): Service =
    new Service(ip = Some(ip))

  def withDependsOn(dependsOn: Service*): Service =
    new Service(dependsOn = dependsOn)

  def withConfig(config: ServiceConfig): Service =
    new Service(config = config)

  // other custom constructors

  def kafkaTopic(clusterName: String, topicName: String): Service =
    Service(
      name = Some(ServiceName(clusterName)),
      `type` = ServiceType.kafka,
      interfaces = Seq(
        ServiceInterface(
          name = Some(InterfaceName(topicName)),
          protocol = Some(Protocol.kafka)
        )
      )
    )
}
