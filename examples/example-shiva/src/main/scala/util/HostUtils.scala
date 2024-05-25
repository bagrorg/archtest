package com.github.mmvpm.examples.shiva
package util

import com.github.mmvpm.core.model._

import scala.util.matching.Regex

object HostUtils {

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
