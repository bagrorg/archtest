package com.github.mmvpm.examples.shiva
package util

import com.github.mmvpm.core.model._

object ServiceUtils {

  def matchServiceByShivaPattern(service: Service, name: ServiceName, interfaceName: InterfaceName): Boolean =
    service.name.contains(name) && service.interfaces.flatMap(_.name).contains(interfaceName)
}
