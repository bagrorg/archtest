package com.github.mmvpm.examples.shiva

import ShivaInstances._

import com.github.mmvpm.core.ArchTest
import com.github.mmvpm.core.model._
import com.github.mmvpm.parsers.shiva.ServicesProviderShiva
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class WordSpec extends AnyWordSpecLike with Matchers with ArchTest with ServicesProviderShiva {

  "topics from service map should occur in config" should {
    services.foreach { service =>
      s"${service.fqn.get}" in {
        val topicsNotExistInConfig = service.dependsOn
          .filter(_.`type` == ServiceType.kafka)
          .filter(topic => !service.hasDependencyInConfig(topic))
          .map(_.interfaces.head.name.get.name)

        topicsNotExistInConfig should have length 0
      }
    }
  }

  "all kafka topic should be used" should {
    val sharedKafka = servicesMap(ServiceFqn.kafka("shared-01"))
    val topics = sharedKafka.interfaces.map { interface =>
      Service.kafkaTopic("shared-01", interface.name.get.name)
    }

    topics.foreach { topic =>
      val topicName = topic.interfaces.head.name.get

      s"$topicName" in {
        val servicesDependsOnTopic = services.filter(_.hasDependencyOn(topic))
        val producersOfTopic = servicesDependsOnTopic.filter { service =>
          (for {
            other <- service.dependsOn
            otherInterface <- other.interfaces
            otherInterfaceName <- otherInterface.name
            if otherInterfaceName.name == topicName.name
            if otherInterface.kafkaType.exists(_.canProduce)
          } yield ()).nonEmpty
        }

        withClue(s"Failed on services ${servicesDependsOnTopic.map(_.fqn.get)}\n") {
          servicesDependsOnTopic.size should be >= 2
          producersOfTopic.size should be >= 1
        }
      }
    }
  }
}
