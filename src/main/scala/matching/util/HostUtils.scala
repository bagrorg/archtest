package com.github.mmvpm
package matching.util

import model.{Service, ServiceType}

import scala.util.matching.Regex

object HostUtils {

  def normalizeHost(host: String): String =
    dropPortAndPath(dropProtocol(host))

  private def dropProtocol(host: String): String =
    List(":///", "://").foldLeft(host) { case (host, delim) =>
      if (host.contains(delim))
        host.drop(host.indexOf(delim) + delim.length)
      else
        host
    }

  private def dropPortAndPath(host: String): String =
    host.takeWhile(!List(':', '/', '?').contains(_))

  def possibleHostPrefixes(service: Service): Seq[String] =
    (service.name, service.`type`) match {
      case (Some(name), ServiceType.service) =>
        service.interfaces.flatMap(_.name).map(s"$name-" + _)
      case _ =>
        Seq.empty
    }

  val InternalHostSuffix = "vertis.yandex.net"

  private val namePattern = "[a-z0-9-_]*"
  private val nameWithTypePattern = "[a-z0-9-_/]*"

  val shivaUrlRegex: Regex =
    s"\\$$\\{url:($nameWithTypePattern):($namePattern)}".r // ${url:name:interface}

  val shivaHostRegex: Regex =
    s"\\$$\\{host:($nameWithTypePattern):($namePattern)}".r // ${host:name:interface}
}
