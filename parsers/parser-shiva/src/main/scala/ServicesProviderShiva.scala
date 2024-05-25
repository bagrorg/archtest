package com.github.mmvpm.parsers.shiva

import converter.ServiceConverter.toService

import com.github.mmvpm.core.ServicesProvider
import com.github.mmvpm.core.model.{Service, ServiceFqn}

trait ServicesProviderShiva extends ServicesProvider {

  private val servicesRoot = "/Users/mmvpm/arcadia/classifieds/services" // TODO

  private val parserYaml = new ParserYaml
  private val parserShiva = new ParserShiva(servicesRoot, parserYaml)

  def getServicesMap: Map[ServiceFqn, Service] = {
    val serviceMaps = parserShiva.parseServiceMaps

    val serviceDeploys = parserShiva.parseDeployConfigs
    val serviceDeploysMap = serviceDeploys.map(c => c.name -> c).toMap

    val services = serviceMaps.map { serviceMap =>
      val serviceDeploy = serviceDeploysMap.get(serviceMap.name)
      val serviceConfig = serviceDeploy.map(parserShiva.parseServiceConfig)
      toService(serviceMap, serviceDeploy, serviceConfig)
    }

    services.map(service => service.fqn.get -> service).toMap
  }
}
