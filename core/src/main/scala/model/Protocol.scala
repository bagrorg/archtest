package com.github.mmvpm.core
package model

final case class Protocol(protocol: String) {
  override def toString: String = protocol
}

object Protocol {

  val http: Protocol =
    Protocol("http")

  val https: Protocol =
    Protocol("https")

  val tcp: Protocol =
    Protocol("tcp")

  val grpc: Protocol =
    Protocol("grpc")

  val kafka: Protocol =
    Protocol("kafka")
}
