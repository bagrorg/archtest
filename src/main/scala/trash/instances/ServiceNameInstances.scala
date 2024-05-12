package com.github.mmvpm
package trash.instances

import matching.Matchable
import model._

object ServiceNameInstances {

  implicit val matchableNameName: Matchable[ServiceName, ServiceName] =
    (a: ServiceName, b: ServiceName) => normalizeName(a) == normalizeName(b)

  private def normalizeName(name: ServiceName): String =
    name.name.toLowerCase
}
