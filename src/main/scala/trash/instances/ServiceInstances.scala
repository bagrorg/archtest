package com.github.mmvpm
package trash.instances

//import matching.Matchable
//import matching.Matchable.Syntax
//import CommonInstances._
//import matching.util.HostUtils.{normalizeHost, possibleHostPrefixes}
//import model._
//
//object ServiceInstances {
//
//  implicit def matchableServiceService(
//      implicit
//      matchFqn: Matchable[ServiceFqn, ServiceFqn],
//      matchHost: Matchable[Host, Host],
//      matchIp: Matchable[Ip, Ip]): Matchable[Service, Service] =
//    (a: Service, b: Service) => {
//      val byFqn = a.fqn.matches(b.fqn)
//      val byHost = a.host.matches(b.host)
//      val byIp = a.ip.matches(b.ip)
//      byFqn || byHost || byIp
//    }
//
//  implicit def matchableServiceKeyValuePair(
//      implicit
//      matchFqn: Matchable[ServiceFqn, KeyValuePair],
//      matchHost: Matchable[Host, KeyValuePair],
//      matchIp: Matchable[Ip, KeyValuePair]): Matchable[Service, KeyValuePair] =
//    (a: Service, b: KeyValuePair) => {
//      val byFqn = a.fqn.matches(b)
//      val byHost = a.host.matches(b)
//      val byIp = a.ip.matches(b)
//      val byHostShiva = matchableByShivaHost.matches(a, b)
//      val byHostInternal = matchableByVertisYandexNet.matches(a, b)
//      byFqn || byHost || byIp || byHostShiva || byHostInternal
//    }
//
//  private val matchableByShivaHost: Matchable[Service, KeyValuePair] =
//    (service: Service, config: KeyValuePair) =>
//      config.value match {
//        case shivaHostRegex(name, interface) =>
//          service.name.exists(_.name == name) && service.interfaces.flatMap(_.name).contains(interface)
//        case shivaUrlRegex(name, interface) =>
//          service.name.exists(_.name == name) && service.interfaces.flatMap(_.name).contains(interface)
//        case _ =>
//          false
//    }
//
//  private val matchableByVertisYandexNet: Matchable[Service, KeyValuePair] =
//    (service: Service, config: KeyValuePair) => {
//      val host = normalizeHost(config.value)
//      possibleHostPrefixes(service).exists { prefix =>
//        host.startsWith(prefix) && host.endsWith(VertisHostSuffix)
//      }
//    }
//
//  private val namePattern = "[a-z0-9-_]*"
//  private val nameWithTypePattern = "[a-z0-9-_/]*"
//  private val shivaUrlRegex = s"\\$$\\{url:($nameWithTypePattern):($namePattern)}".r // ${url:name:interface}
//  private val shivaHostRegex = s"\\$$\\{host:($nameWithTypePattern):($namePattern)}".r // ${host:name:interface}
//
//  private val VertisHostSuffix = "vertis.yandex.net"
//}
