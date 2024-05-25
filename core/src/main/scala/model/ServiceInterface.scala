package com.github.mmvpm.core
package model

case class ServiceInterface(
    name: Option[String] = None,
    protocol: Option[String] = None,
    port: Option[Int] = None,
    kafkaProducer: Option[Boolean] = None)
