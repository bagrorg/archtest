package com.github.mmvpm
package parser.model

import parser.model.ServiceMap._

import io.circe.Decoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredDecoder

case class ServiceMap(
    name: String,
    description: String,
    owners: List[String],
    aYaml: Option[AYaml],
    chatDuty: Option[String],
    dashboards: Option[List[Dashboard]],
    dependsOn: Option[List[DependsOn]],
    designDocument: Option[String],
    duty: Option[Duty],
    externalUrl: Option[ExternalUrl],
    groups: Option[List[String]],
    isExternal: Option[Boolean],
    language: Option[String],
    links: Option[List[Link]],
    mdbCluster: Option[MdbCluster],
    options: Option[Options],
    pciDss: Option[Boolean],
    provides: Option[List[Provides]],
    readonlyFilesystem: Option[Boolean],
    share: Option[Boolean],
    sox: Option[Boolean],
    src: Option[String],
    startrek: Option[String],
    `type`: Option[String])

object ServiceMap {

  case class AYaml(
      path: String,
      releaseName: String,
      service: String)

  case class Dashboard(
      name: String,
      url: String)

  case class DependsOn(
      service: String,
      interfaceName: String,
      expectedRps: Option[Int],
      failureReaction: Option[FailureReaction],
      kafkaProducer: Option[Boolean])

  case class FailureReaction(
      errors: String,
      missing: String,
      timeout: String,
      unexpectedResult: String)

  case class Duty(
      chatDuty: Option[String],
      scheduleSlug: Option[String],
      serviceSlug: Option[String])

  case class ExternalUrl(
      prod: String,
      prodBranch: Option[String],
      test: String,
      testBranch: Option[String])

  case class Link(
      name: String,
      url: String)

  case class MdbCluster(
      prodId: Option[String],
      testId: Option[String])

  case class Options(
      layer: Option[String],
      keep: Option[Int],
      schedule: Option[String],
      target: Option[String],
      include: Option[List[String]],
      prod: Option[LayerConfig],
      test: Option[LayerConfig])

  case class LayerConfig(
      folderId: String,
      `class`: String,
      diskSize: Int)

  case class Provides(
      name: String,
      description: String,
      protocol: String,
      apiDoc: Option[String],
      options: Option[ProvidesOptions],
      port: Option[Int],
      slaRps: Option[Int],
      slaTimingMean: Option[Int],
      slaTimingP99: Option[Int],
      tags: Option[List[String]])

  case class ProvidesOptions(
      partitions: Int,
      `retention.ms`: Option[Int])

  // circe instances

  implicit val customConfig: Configuration =
    Configuration.default.withDefaults.withSnakeCaseMemberNames

  implicit val decoderAYaml: Decoder[AYaml] = deriveConfiguredDecoder
  implicit val decoderDashboard: Decoder[Dashboard] = deriveConfiguredDecoder
  implicit val decoderFailureReaction: Decoder[FailureReaction] = deriveConfiguredDecoder
  implicit val decoderDependsOn: Decoder[DependsOn] = deriveConfiguredDecoder
  implicit val decoderDuty: Decoder[Duty] = deriveConfiguredDecoder
  implicit val decoderExternalUrl: Decoder[ExternalUrl] = deriveConfiguredDecoder
  implicit val decoderLink: Decoder[Link] = deriveConfiguredDecoder
  implicit val decoderMdbCluster: Decoder[MdbCluster] = deriveConfiguredDecoder
  implicit val decoderLayerConfig: Decoder[LayerConfig] = deriveConfiguredDecoder
  implicit val decoderOptions: Decoder[Options] = deriveConfiguredDecoder
  implicit val decoderProvidesOptions: Decoder[ProvidesOptions] = deriveConfiguredDecoder
  implicit val decoderProvides: Decoder[Provides] = deriveConfiguredDecoder
  implicit val decoderServiceMap: Decoder[ServiceMap] = deriveConfiguredDecoder
}
