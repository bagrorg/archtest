package com.github.mmvpm

import com.github.mmvpm.trash.instances.HostInstances._
import com.github.mmvpm.trash.instances.IpInstances._
import com.github.mmvpm.trash.instances.ServiceFqnInstances._
import com.github.mmvpm.trash.instances.ServiceInstances._
import model.{KeyValuePair, Service, ServiceFqn}

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

  "test" should "diff" in {
    val salesmanApi = services(ServiceFqn.service("salesman-api"))

    val result = services.values
      .filter { service =>
        service.hasDependencyOn(salesmanApi)
      }
      .map(_.fqn.get.fqn)
      .toSet

    val result2 = services.values
      .filter { service =>
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
      val dependenciesFromConfig = services.values.filter(service.hasDependencyInConfigOn).map(_.fqn.get.fqn).toSet

      withClue(s"Service ${service.fqn.get}: ") {
        dependsOnSection.diff(dependenciesFromConfig) should have size 0
      }
    }
  }
}
