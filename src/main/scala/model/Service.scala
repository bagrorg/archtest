package com.github.mmvpm
package model

import matching.Instances.{matchServiceConfig, matchServices}
import matching.Matchable
import matching.Matchable.Matchable1
import model.ServiceInterface.fromProvides
import parser.model.{ServiceConfig => ServiceConfigRaw, ServiceDeploy, ServiceMap}
import parser.model.ServiceMap.DependsOn

case class Service(
    name: Option[ServiceName] = None,
    `type`: ServiceType = ServiceType.service,
    interfaces: Seq[ServiceInterface] = Seq.empty,
    host: Option[Host] = None,
    ip: Option[Ip] = None,
    dependsOn: Seq[Service] = Seq.empty,
    config: ServiceConfig = ServiceConfig.empty) {

  val fqn: Option[ServiceFqn] = {
    val prefix = `type` match {
      case ServiceType.service => ""
      case ServiceType(rawType) => rawType + "/"
    }
    name.map(prefix + _).map(ServiceFqn(_))
  }

  // match with raw config

  def existsInConfig(predicate: KeyValuePair => Boolean): Boolean =
    config.all.params.exists(predicate)

  def existsInConfigProd(predicate: KeyValuePair => Boolean): Boolean =
    config.prodWithCommon.params.exists(predicate)

  def existsInConfigTest(predicate: KeyValuePair => Boolean): Boolean =
    config.testWithCommon.params.exists(predicate)

  // match with other service

  def hasDependencyOn(
      other: Service
    )(implicit
      matchableForServices: Matchable1[Service] = matchServices,
      matchableForConfigs: Matchable[Service, KeyValuePair] = matchServiceConfig): Boolean =
    hasDependencyInServiceMap(other)(matchableForServices) || hasDependencyInConfig(other)(matchableForConfigs)

  def hasDependencyInServiceMap(
      other: Service
    )(implicit matchable: Matchable1[Service] = matchServices): Boolean =
    dependsOn.exists(matchable.matches(other, _))

  def hasDependencyInConfig(
      other: Service
    )(implicit matchable: Matchable[Service, KeyValuePair] = matchServiceConfig): Boolean =
    config.all.params.exists(matchable.matches(other, _))
}

object Service {

  def withIp(ip: String): Service =
    new Service(ip = Some(Ip(ip)))

  def withName(name: String): Service =
    new Service(name = Some(ServiceName(name)))

  def withHost(host: String): Service =
    new Service(host = Some(Host(host)))

  def fromPaas(
      serviceMap: ServiceMap,
      deployConfig: Option[ServiceDeploy],
      serviceConfig: Option[ServiceConfigRaw]): Service =
    Service(
      name = Some(ServiceName(serviceMap.name)),
      `type` = ServiceType.from(serviceMap.`type`),
      interfaces = serviceMap.provides.getOrElse(Seq.empty).map(fromProvides),
      dependsOn = serviceMap.dependsOn.getOrElse(Seq.empty).map(fromDependsOn),
      config = ServiceConfig.from(serviceConfig.getOrElse(ServiceConfigRaw.empty))
    )

  def fromDependsOn(dependsOn: DependsOn): Service = {
    val (serviceTypeRaw, name) =
      dependsOn.service.split("/").takeRight(2) match {
        case Array(name) => (None, name)
        case Array(serviceType, name) => (Some(serviceType), name)
      }
    val serviceType = ServiceType.from(serviceTypeRaw)

    val interface = ServiceInterface(name = Some(dependsOn.interfaceName))

    Service(
      name = Some(ServiceName(name)),
      `type` = serviceType,
      interfaces = Seq(interface)
    )
  }
}
