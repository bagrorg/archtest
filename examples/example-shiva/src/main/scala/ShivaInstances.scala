package com.github.mmvpm.examples.shiva

import util.HostUtils._

import com.github.mmvpm.core.matching.{Instances, Matchable}
import com.github.mmvpm.core.matching.util.HostUtils._
import com.github.mmvpm.core.model._

object ShivaInstances {

  val byShivaHost: Matchable[Service, KeyValuePair] =
    (service: Service, config: KeyValuePair) =>
      config.value match {
        case shivaHostRegex(name, interface) =>
          service.name.exists(_.name == name) && service.interfaces.flatMap(_.name).contains(interface)
        case shivaUrlRegex(name, interface) =>
          service.name.exists(_.name == name) && service.interfaces.flatMap(_.name).contains(interface)
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

  implicit val matchServices: Matchable[Service, Service] =
    Instances.matchServices

  implicit val matchServiceConfig: Matchable[Service, KeyValuePair] =
    Instances.matchServiceConfig or byShivaHost or byInternalHost
}
