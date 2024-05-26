package com.github.mmvpm.examples.shiva

import util.HostUtils._
import util.ServiceUtils.matchServiceByShivaPattern

import com.github.mmvpm.core.matching.{Instances, Matchable}
import com.github.mmvpm.core.matching.util.HostUtils._
import com.github.mmvpm.core.model._

object ShivaInstances {

  val byShivaHost: Matchable[Service, KeyValuePair] =
    (service: Service, config: KeyValuePair) =>
      config.value match {
        case shivaHostRegex(name, interface) =>
          matchServiceByShivaPattern(service, ServiceName(name), InterfaceName(interface))
        case shivaUrlRegex(name, interface) =>
          matchServiceByShivaPattern(service, ServiceName(name), InterfaceName(interface))
        case _ =>
          false
      }

  val byInternalHost: Matchable[Service, KeyValuePair] =
    (service: Service, config: KeyValuePair) => {
      val host = normalizeHost(config.value)
      possibleHostPrefixes(service).exists { prefix =>
        host.startsWith(prefix) && host.endsWith(InternalHostSuffix)
      }
    }

  val byKafkaTopic: Matchable[Service, KeyValuePair] =
    (service: Service, config: KeyValuePair) => {
      val isKafkaService = service.`type` == ServiceType.kafka

      val topics = service.interfaces.flatMap(_.name).map(_.name)
      val configValueIsTopicName = topics.contains(config.value)

      val key = config.key.toLowerCase
      val configKeyRelatedToKafka = key.contains("kafka") || key.contains("topic")

      isKafkaService && configValueIsTopicName && configKeyRelatedToKafka
    }

  val matchAnyExternalService: Matchable[Service, KeyValuePair] =
    (service: Service, config: KeyValuePair) => {
      // by host
      val hostKeywords = Seq(".net", ".com", ".ru", "://", ":80", "/api")
      val isHost = hostKeywords.exists(config.value.contains)

      val host = normalizeHost(config.value).toLowerCase
      val internalKeywords = Seq("yandex", "vertis", "auto", "avto", "consul", "cm.expert")
      val isInternalHost = internalKeywords.exists(host.contains)

      // by ip
      val ipParts = config.value.split('.')
      val arePartsValid = ipParts.forall(_.toIntOption.exists(i => 0 <= i && i < 256))
      val isIpAddress = ipParts.length == 4 && arePartsValid

      val isExternal = isHost && !isInternalHost || isIpAddress

      service.isExternal && isExternal
    }

  implicit val matchServices: Matchable[Service, Service] =
    Instances.matchServices

  implicit val matchServiceConfig: Matchable[Service, KeyValuePair] =
    Instances.matchServiceConfig or byShivaHost or byInternalHost or byKafkaTopic
}
