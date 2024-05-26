package com.github.mmvpm.core
package model

sealed trait KafkaType

object KafkaType {

  final case object Unknown extends KafkaType

  final case object Consumer extends KafkaType

  final case object Producer extends KafkaType

  final case object ProducerAndConsumer extends KafkaType
}
