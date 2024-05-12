package com.github.mmvpm
package matching.util

import model.{Service, ServiceType}

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
}
