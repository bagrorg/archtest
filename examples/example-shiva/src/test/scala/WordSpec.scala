package com.github.mmvpm.examples.shiva

import ShivaInstances._

import com.github.mmvpm.core.ArchTest
import com.github.mmvpm.core.model._
import com.github.mmvpm.parsers.shiva.ServicesProviderShiva
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class WordSpec extends AnyWordSpecLike with Matchers with ArchTest with ServicesProviderShiva {

  "topics from service map should occur in config" ignore {
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

  "all kafka topic should be used" ignore {
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

  "all mysql databases should be used" ignore {
    val mysqlDatabases = services.filter(_.`type` == ServiceType.mysql)

    mysqlDatabases.foreach { db =>
      s"${db.fqn.get}" in {
        services.filter(_.hasDependencyOn(db)) should not be empty
      }
    }
  }

  "all postgresql databases should be used" ignore {
    val mysqlDatabases = services.filter(_.`type` == ServiceType.postgresql)

    mysqlDatabases.foreach { db =>
      s"${db.fqn.get}" in {
        services.filter(_.hasDependencyOn(db)) should not be empty
      }
    }
  }

  "mysql database should be accessible from at most one service" should {
    val mysqlDatabases = services.filter(_.`type` == ServiceType.mysql)

    mysqlDatabases.foreach { mysqlDatabase =>
      s"${mysqlDatabase.fqn.get}" in {
        val servicesDependsOnDb = services
          .filter(_.hasDependencyOn(mysqlDatabase))
          .filter(_.`type` != ServiceType.etl)

        withClue(
          s"Failed on services ${servicesDependsOnDb.map(s => s"${s.fqn.get} (${s.groups.mkString(", ")})")}\n"
        ) {
          lazy val groupsIntersection = servicesDependsOnDb
            .map(_.groups)
            .reduce(_ intersect _)
          val allFromTheSameGroup = servicesDependsOnDb.size <= 1 || groupsIntersection.nonEmpty

          allFromTheSameGroup shouldBe true
        }
      }
    }
  }
}
