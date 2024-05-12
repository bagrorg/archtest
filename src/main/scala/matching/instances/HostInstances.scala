package com.github.mmvpm
package matching.instances

import matching.Matchable
import matching.util.HostUtils.normalizeHost
import model._

object HostInstances {

  implicit val matchableHostHost: Matchable[Host, Host] =
    (a: Host, b: Host) => normalizeHost(a.host) == normalizeHost(b.host)

  implicit val matchableHostKeyValuePair: Matchable[Host, KeyValuePair] =
    (a: Host, b: KeyValuePair) => normalizeHost(a.host) == normalizeHost(b.value)
}
