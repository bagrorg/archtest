package com.github.mmvpm
package parser.model

import parser.model.ServiceDeploy._

import io.circe.Decoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredDecoder

case class ServiceDeploy(
    name: String,
    image: Option[String],
    canary: Option[Canary],
    commonParams: Option[Params],
    general: Option[Layer],
    prod: Option[Layer],
    test: Option[Layer],
    productionMirroring: Option[Boolean],
    readonlyFilesystem: Option[Boolean],
    secretsFromEnv: Option[Boolean])

object ServiceDeploy {

  case class Canary(promote: String)

  case class Layer(
      resources: Option[Resources],
      config: Option[ConfigSources],
      params: Option[Params],
      datacenters: Option[Datacenters],
      geobaseVersion: Option[Int],
      periodic: Option[String],
      readonlyFilesystem: Option[Boolean],
      systemJobProperties: Option[SystemJobProperties],
      traceProject: Option[String],
      upgrade: Option[Upgrade])

  case class ConfigSources(
      files: Option[List[String]],
      params: Option[Params])

  case class Datacenters(
      any: Option[Datacenter],
      sas: Option[Datacenter],
      vla: Option[Datacenter])

  case class Datacenter(count: Int)

  case class Resources(
      cpu: Option[Int],
      gpu: Option[Int],
      cores: Option[Int],
      memory: Option[Int],
      autoCpu: Option[Boolean],
      cpuHardLimit: Option[Boolean])

  case class SystemJobProperties(
      additionalMounts: Option[String],
      allClientsJob: Option[Boolean],
      customMonitoringPort: Option[Int])

  case class Upgrade(
      parallel: Option[Int],
      seamlessDeploy: Option[Boolean])

  // circe instances

  implicit val customConfig: Configuration =
    Configuration.default.withDefaults.withSnakeCaseMemberNames

  implicit val decoderCanary: Decoder[Canary] = deriveConfiguredDecoder
  implicit val decoderConfigSources: Decoder[ConfigSources] = deriveConfiguredDecoder
  implicit val decoderDatacenter: Decoder[Datacenter] = deriveConfiguredDecoder
  implicit val decoderDatacenters: Decoder[Datacenters] = deriveConfiguredDecoder
  implicit val decoderResources: Decoder[Resources] = deriveConfiguredDecoder
  implicit val decoderSystemJobProperties: Decoder[SystemJobProperties] = deriveConfiguredDecoder
  implicit val decoderUpgrade: Decoder[Upgrade] = deriveConfiguredDecoder
  implicit val decoderLayer: Decoder[Layer] = deriveConfiguredDecoder
  implicit val decoderServiceDeploy: Decoder[ServiceDeploy] = deriveConfiguredDecoder
}
