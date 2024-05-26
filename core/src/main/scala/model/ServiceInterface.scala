package com.github.mmvpm.core
package model

case class ServiceInterface(
    name: Option[InterfaceName] = None,
    protocol: Option[Protocol] = None,
    port: Option[Int] = None,
    kafkaType: Option[KafkaType] = None)
