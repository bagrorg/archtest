package com.github.mmvpm.parsers.shiva
package converter

import model._
import model.ShivaServiceMap.{DependsOn, Provides}

import com.github.mmvpm.core.model._
import com.github.mmvpm.core.model.ServiceConfig.Layer

object ServiceConverter {

  def toService(
      serviceMap: ShivaServiceMap,
      deployConfig: Option[ShivaServiceDeploy],
      serviceConfig: Option[ShivaServiceConfig]): Service =
    Service(
      name = Some(ServiceName(serviceMap.name)),
      `type` = ServiceType.from(serviceMap.`type`.map(_.stripPrefix("mdb_"))),
      interfaces = serviceMap.provides.getOrElse(Seq.empty).map(_.toServiceInterface),
      dependsOn = serviceMap.dependsOn.getOrElse(Seq.empty).map(_.toService),
      config = serviceConfig.getOrElse(ShivaServiceConfig.empty).toServiceConfig
    )

  implicit class ProvidesConverter(provides: Provides) {

    def toServiceInterface: ServiceInterface =
      ServiceInterface(
        name = Some(InterfaceName(provides.name)),
        protocol = Some(Protocol(provides.protocol)),
        port = provides.port
      )
  }

  implicit class DependsOnConverter(dependsOn: DependsOn) {

    def toService: Service =
      Service
        .withFqn(dependsOn.service)
        .withInterfaces(
          ServiceInterface(
            name = Some(InterfaceName(dependsOn.interfaceName)),
            kafkaType = dependsOn.kafkaProducer.map {
              case false => KafkaType.Consumer
              case true => KafkaType.ProducerAndConsumer
            }
          )
        )
  }

  implicit class ShivaServiceConfigConverter(config: ShivaServiceConfig) {

    def toServiceConfig: ServiceConfig =
      ServiceConfig(
        common = convertConfigLayer(config.common),
        testOnly = convertConfigLayer(config.testOnly),
        prodOnly = convertConfigLayer(config.prodOnly)
      )

    private def convertConfigLayer(raw: Params): Layer =
      Layer(
        raw.toSeq.collect { case (key, value) =>
          val valueAsString = value.fold(
            jsonNull = "null",
            jsonBoolean = _.toString,
            jsonNumber = _.toString,
            jsonString = identity,
            jsonArray = _ => value.toString,
            jsonObject = _ => value.toString
          )
          KeyValuePair(key, valueAsString)
        }
      )
  }
}
