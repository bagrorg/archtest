package com.github.mmvpm.core
package model

sealed trait KafkaType {
  def canProduce: Boolean
}

object KafkaType {

  final case object Unknown extends KafkaType {
    def canProduce: Boolean = false
  }

  final case object Consumer extends KafkaType {
    def canProduce: Boolean = false
  }

  final case object Producer extends KafkaType {
    def canProduce: Boolean = true
  }

  final case object ProducerAndConsumer extends KafkaType {
    def canProduce: Boolean = true
  }
}
