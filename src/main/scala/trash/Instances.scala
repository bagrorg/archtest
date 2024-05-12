package com.github.mmvpm
package matching

//import model.Service
//
//object Instances {
//
//  implicit val matchableServiceService: Matchable[Service, Service] = ???
//  implicit val matchableServiceString: Matchable[Service, String] = ???
//
//  implicit def instance(implicit ev: Matchable[Service, Service]): CanHasDependency[Service] =
//    (current: Service, other: Service) => {
//      current.dependsOn.exists { serviceFromDependsOn =>
//        ev.matches(serviceFromDependsOn, other)
//      }
//
//      ???
//    }
//}
