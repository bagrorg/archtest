package com.github.mmvpm.core

import model.{Service, ServiceFqn}

trait ServicesProvider {
  def getServicesMap: Map[ServiceFqn, Service]
}
