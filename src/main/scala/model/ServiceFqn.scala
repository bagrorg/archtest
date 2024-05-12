package com.github.mmvpm
package model

final case class ServiceFqn(fqn: String) extends AnyVal {
  override def toString: String = fqn
}

object ServiceFqn {

  def service(name: String): ServiceFqn =
    ServiceFqn(name)

  def batch(name: String): ServiceFqn =
    ServiceFqn("batch/" + name)

  def kafka(name: String): ServiceFqn =
    ServiceFqn("kafka/" + name)
}
