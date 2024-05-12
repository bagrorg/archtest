package com.github.mmvpm
package model

import matching.Instances.{matchServiceConfig, matchServices}
import matching.Matchable
import model.ServiceInterface.fromProvides
import parser.model.{ServiceDeploy, ServiceMap, ServiceConfig => ServiceConfigRaw}
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
      forServices: Matchable[Service, Service] = matchServices,
      forConfigs: Matchable[Service, KeyValuePair] = matchServiceConfig): Boolean =
    hasDependencyInServiceMap(other)(forServices) || hasDependencyInConfig(other)(forConfigs)

  def hasDependencyInServiceMap(
      other: Service
    )(implicit matchable: Matchable[Service, Service] = matchServices): Boolean =
    dependsOn.exists(matchable.matches(other, _))

  def hasDependencyInConfig(
      other: Service
    )(implicit matchable: Matchable[Service, KeyValuePair] = matchServiceConfig): Boolean =
    config.all.params.exists(matchable.matches(other, _))
}

object Service {

  def withIp(ip: String): Service =
    new Service(ip = Some(Ip(ip)))

  def withFqn(fqn: String): Service = {
    val (serviceType, name) = parseFqn(fqn)
    new Service(name = Some(name), `type` = serviceType)
  }

  def withHost(host: String): Service =
    new Service(host = Some(Host(host)))

  def kafkaTopic(clusterName: String, topicName: String): Service =
    Service(
      name = Some(ServiceName(clusterName)),
      `type` = ServiceType.kafka,
      interfaces = Seq(
        ServiceInterface(
          name = Some(topicName),
          protocol = Some("kafka")
        )
      )
    )

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
    val (serviceType, name) = parseFqn(dependsOn.service)
    val interface = ServiceInterface(name = Some(dependsOn.interfaceName))
    Service(
      name = Some(name),
      `type` = serviceType,
      interfaces = Seq(interface)
    )
  }

  private def parseFqn(fqn: String): (ServiceType, ServiceName) = {
    val (typeRaw, nameRaw) =
      fqn.split("/").takeRight(2) match {
        case Array(name) => (None, name)
        case Array(serviceType, name) => (Some(serviceType), name)
      }
    (ServiceType.from(typeRaw), ServiceName(nameRaw))
  }
}
