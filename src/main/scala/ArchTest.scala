package com.github.mmvpm

import model.{Service, ServiceFqn}
import parser.{PaasParser, YamlParser}

trait ArchTest {

  private val servicesRoot = "/Users/mmvpm/arcadia/classifieds/services" // TODO

  private val yamlParser = new YamlParser
  private val paasParser = new PaasParser(servicesRoot, yamlParser)

  val services: Map[ServiceFqn, Service] = {
    val serviceMaps = paasParser.parseServiceMaps

    val deployConfigs = paasParser.parseDeployConfigs
    val deployConfigsMap = deployConfigs.map(c => c.name -> c).toMap

    val services = serviceMaps.map { serviceMap =>
      val deployConfig = deployConfigsMap.get(serviceMap.name)
      val serviceConfig = deployConfig.map(paasParser.parseServiceConfig)
      Service.fromPaas(serviceMap, deployConfig, serviceConfig)
    }

    services.map(service => service.fqn.get -> service).toMap
  }
}
