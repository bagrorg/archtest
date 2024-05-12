package com.github.mmvpm
package matching.instances

import matching.Matchable
import matching.Matchable.Syntax
import model._

object Instances {

  import CommonInstances._
  import HostInstances._
  import ServiceNameInstances._
  import ServiceFqnInstances._
  import IpInstances._
  import ServiceInstances._

//  implicit def matchableOptions[A, B](implicit ev: Matchable[A, B]): Matchable[Option[A], Option[B]] =
//    CommonInstances.matchableOptions
//
//  implicit val matchableIpIp: Matchable[Ip, Ip] = IpInstances.matchableIpIp
}
