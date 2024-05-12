package com.github.mmvpm
package matching

//import model.{Host, Service}
//
//object ServiceMatcher {
//
//  def matches(service: Service, config: String): Boolean = {
//    val byIp = ipMatches(service, Service.withIp(config))
//    val byHostRaw = hostMatches(service, Service.withHost(config))
//    val byHostShiva = shivaHostMatches(service, config)
//    byIp || byHostRaw || byHostShiva
//  }
//
//  def matches(first: Service, second: Service): Boolean = {
//    val byFqn = fqnMatches(first, second)
//    val byIp = ipMatches(first, second)
//    val byHost = hostMatches(first, second)
//    byFqn || byIp || byHost
//  }
//
//  private def fqnMatches(first: Service, second: Service): Boolean =
//    first.fqn.nonEmpty && first.fqn == second.fqn
//
//  private def ipMatches(first: Service, second: Service): Boolean =
//    first.ip.nonEmpty && first.ip == second.ip
//
//  private def hostMatches(first: Service, second: Service): Boolean = // without protocol and port
//    first.host.nonEmpty && first.host.map(normalize) == second.host.map(normalize)
//
//  private def normalize(host: Host): String =
//    dropPortAndPath(dropProtocol(host.host))
//
//  private def dropProtocol(host: String): String =
//    List(":///", "://").foldLeft(host) { case (host, delim) =>
//      if (host.contains(delim))
//        host.drop(host.indexOf(delim) + delim.length)
//      else
//        host
//    }
//
//  private def dropPortAndPath(host: String): String =
//    host.takeWhile(!List(':', '/', '?').contains(_))
//
//  private def shivaHostMatches(service: Service, config: String): Boolean =
//    config match {
//      case shivaHostRegex(name, interface) =>
//        service.name.exists(_.name == name) && service.interfaces.flatMap(_.name).contains(interface)
//      case _ =>
//        false
//    }
//
//  private val namePattern = "[a-z0-9-_]*"
//  private val nameWithTypePattern = "[a-z0-9-_/]*"
//  private val shivaHostRegex = s"$$\\{host:($nameWithTypePattern):($namePattern)}".r // ${host:name:interface}
//}
