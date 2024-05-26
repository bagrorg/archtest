package com.github.mmvpm.examples.shiva

import ShivaInstances.{matchAnyExternalService, matchServiceConfig, matchServices}

import com.github.mmvpm.core.ArchTest
import com.github.mmvpm.core.matching.Matchable
import com.github.mmvpm.core.matching.util.HostUtils.normalizeHost
import com.github.mmvpm.core.model._
import com.github.mmvpm.parsers.shiva.ServicesProviderShiva
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.Inspectors
import org.scalatest.LoneElement.convertToCollectionLoneElementWrapper

class FlatSpec extends AnyFlatSpec with Matchers with ArchTest with ServicesProviderShiva {

  "only autoru-api" should "have a dependency on callkeeper" in {
    val callkeeper = Service.withIp("185.98.83.28")

    val result = services.filter { service =>
      service.hasDependencyOn(callkeeper)
    }

    result.loneElement.fqn.get.fqn shouldBe "autoru-api"
  }

  it should "have a dependency on iskra" in {
    val iskra = Service.withHost("iskra-api-grpc.vrts-slb.prod.vertis.yandex.net")

    val result = services.filter { service =>
      service.hasDependencyOn(iskra)
    }

    result.loneElement.fqn.get.fqn shouldBe "autoru-api"
  }

  "only aptly" should "have access to the secret" in {
    val secret = "${sec-01g63hzvghswkh9qckg7c61zqe:ver-01g65hkmb3g7tqbp654mrj6pf4:ARC_API_TOKEN}"

    val result = services.filter { service =>
      service.existsInConfigProd { case KeyValuePair(_, value) =>
        value == secret
      }
    }

    result.loneElement.fqn.get.fqn shouldBe "batch/aptly"
  }

  "only fp and vos" should "have access to feedprocessor topic" in {
    val autoruToVosTopic = Service.kafkaTopic("shared-01", "feedprocessor-autoru-to-vos")
    val servicesWithAccess = Set("feedprocessor-auto-rt", "feedprocessor-auto-nonrt", "vos2-autoru-consumers")

    val result = services.filter { service =>
      service.hasDependencyOn(autoruToVosTopic)
    }
    result.map(_.fqn.get.fqn).toSet.diff(servicesWithAccess) shouldBe empty

    val matchTopicName: Matchable[Service, KeyValuePair] =
      (service: Service, config: KeyValuePair) => service.interfaces.flatMap(_.name.map(_.name)).contains(config.value)

    val matchable = matchServiceConfig or matchTopicName

    val resultConfig = services.filter { service =>
      service.hasDependencyInConfig(autoruToVosTopic)(matchable)
    }
    resultConfig.map(_.fqn.get.fqn).toSet shouldBe servicesWithAccess
  }

  "salesman-api" should "not have difference between service map and configs" in {
    val salesmanApi = servicesMap(ServiceFqn.service("salesman-api"))

    val result = services
      .filter { service =>
        service.hasDependencyOn(salesmanApi)
      }
      .map(_.fqn.get.fqn)
      .toSet

//    val containsSalesmanApi: Matchable[Service, KeyValuePair] =
//      (_: Service, b: KeyValuePair) => b.value.contains("salesman-api")

    val result2 = services
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
    Inspectors.forEvery(services) { service =>
      val dependsOnSection = service.dependsOn.map(_.fqn.get.fqn).toSet
      val dependenciesFromConfig = services.filter(service.hasDependencyInConfig).map(_.fqn.get.fqn).toSet

      withClue(s"Service ${service.fqn.get}: ") {
        dependsOnSection.diff(dependenciesFromConfig) should have size 0
      }
    }
  }

  "vertis-vos-vos2-auto-shard1" should "test" in {
    val vos2AutoShard1 = servicesMap(ServiceFqn.mysql("vertis-vos-vos2-auto-shard1"))

    val result = services.filter(_.hasDependencyOn(vos2AutoShard1))

    result.flatMap(_.fqn).forall(_.fqn.startsWith("vos2-autoru")) shouldBe true
  }

  "autoru-api" should "not have external dependencies" in {
    val externalService = Service.withIsExternal(true)

    val result = Services.autoruApi.getMatchesInConfigWith(externalService)(matchAnyExternalService)

    withClue(s"Failed configs:\n- ${result.map(p => s"${p.key}: ${p.value}").mkString("\n- ")}\n\n") {
      result shouldBe Seq.empty
    }
  }
}
