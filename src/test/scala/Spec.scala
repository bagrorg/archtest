package com.github.mmvpm

import matching.Instances._
import model.{KeyValuePair, Service, ServiceFqn, ServiceInterface, ServiceName, ServiceType}

import com.github.mmvpm.matching.Matchable
import com.github.mmvpm.matching.Matchable.byEquals
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.Inspectors
import org.scalatest.LoneElement.convertToCollectionLoneElementWrapper

class Spec extends AnyFlatSpec with Matchers with ArchTest {

  "only autoru-api" should "have a dependency on callkeeper" in {
    val callkeeper = Service.withIp("185.98.83.28")

    val result = services.values.filter { service =>
      service.hasDependencyOn(callkeeper)
    }.toList

    result.loneElement.fqn.get.fqn shouldBe "autoru-api"
  }

  it should "have a dependency on iskra" in {
    val iskra = Service.withHost("iskra-api-grpc.vrts-slb.prod.vertis.yandex.net")

    val result = services.values.filter { service =>
      service.hasDependencyOn(iskra)
    }.toList

    result.loneElement.fqn.get.fqn shouldBe "autoru-api"
  }

  "only aptly" should "have access to the secret" in {
    val secret = "${sec-01g63hzvghswkh9qckg7c61zqe:ver-01g65hkmb3g7tqbp654mrj6pf4:ARC_API_TOKEN}"

    val result = services.values.filter { service =>
      service.existsInConfigProd { case KeyValuePair(_, value) =>
        value == secret
      }
    }

    result.loneElement.fqn.get.fqn shouldBe "batch/aptly"
  }

  "only fp and vos" should "have access to feedprocessor topic" in {
    val autoruToVosTopic = Service.kafkaTopic("shared-01", "feedprocessor-autoru-to-vos")
    val servicesWithAccess = Set("feedprocessor-auto-nonrt", "vos2-autoru-consumers")

    val result = services.values.filter { service =>
      service.hasDependencyOn(autoruToVosTopic)
    }
    result.map(_.fqn.get.fqn).toSet shouldBe servicesWithAccess

    val matchTopicName: Matchable[Service, KeyValuePair] =
      (service: Service, config: KeyValuePair) => service.interfaces.flatMap(_.name).contains(config.value)

    val matchable = matchServiceConfig.or(matchTopicName)

    val resultConfig = services.values.filter { service =>
      service.hasDependencyInConfig(autoruToVosTopic)(matchable)
    }
    println(resultConfig.map(_.fqn.get))
    resultConfig.map(_.fqn.get.fqn).toSet shouldBe servicesWithAccess
  }

  "test" should "diff" in {
    val salesmanApi = services(ServiceFqn.service("salesman-api"))

    val result = services.values
      .filter { service =>
        service.hasDependencyOn(salesmanApi)
      }
      .map(_.fqn.get.fqn)
      .toSet

//    val containsSalesmanApi: Matchable[Service, KeyValuePair] =
//      (_: Service, b: KeyValuePair) => b.value.contains("salesman-api")

    val result2 = services.values
      .filter { service =>
//        service.hasDependencyInConfig(salesmanApi)(containsSalesmanApi)
        service.existsInConfig(_.value.contains("salesman-api"))
      }
      .map(_.fqn.get.fqn)
      .toSet

    println(s"sizes: ${result.size}, ${result2.size}")
    println(s"conf - depOn = ${result2.diff(result)}")
    println(s"depOn - conf = ${result.diff(result2)}")
  }

  "dependsOn" should "not contain irrelevant dependencies" ignore {
    Inspectors.forEvery(services.values) { service =>
      val dependsOnSection = service.dependsOn.map(_.fqn.get.fqn).toSet
      val dependenciesFromConfig = services.values.filter(service.hasDependencyInConfig).map(_.fqn.get.fqn).toSet

      withClue(s"Service ${service.fqn.get}: ") {
        dependsOnSection.diff(dependenciesFromConfig) should have size 0
      }
    }
  }
}
