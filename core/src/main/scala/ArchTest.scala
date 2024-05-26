package com.github.mmvpm.core

import model.{Service, ServiceFqn}

trait ArchTest { self: ServicesProvider =>

  lazy val servicesMap: Map[ServiceFqn, Service] =
    self.getServicesMap

  lazy val services: Seq[Service] =
    servicesMap.values.toSeq

  object Services {

    lazy val autoruApi: Service =
      servicesMap(ServiceFqn.service("autoru-api"))
  }
}
