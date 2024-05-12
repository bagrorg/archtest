package com.github.mmvpm
package trash.instances

import matching.Matchable
import model._

object ServiceFqnInstances {

  implicit val matchableFqnFqn: Matchable[ServiceFqn, ServiceFqn] =
    (a: ServiceFqn, b: ServiceFqn) => normalizeFqn(a) == normalizeFqn(b)

  implicit val fakeMatchableFqnKeyValuePair: Matchable[ServiceFqn, KeyValuePair] =
    (a: ServiceFqn, b: KeyValuePair) => false

  private def normalizeFqn(fqn: ServiceFqn): String =
    fqn.fqn.toLowerCase
}
