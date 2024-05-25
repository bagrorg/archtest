package com.github.mmvpm.core
package matching.util

object HostUtils {

  def normalizeHost(host: String): String =
    dropPortAndPath(dropProtocol(host))

  private def dropProtocol(host: String): String =
    List(":///", "://").foldLeft(host) { case (host, delim) =>
      if (host.contains(delim))
        host.drop(host.indexOf(delim) + delim.length)
      else
        host
    }

  private def dropPortAndPath(host: String): String =
    host.takeWhile(!List(':', '/', '?').contains(_))
}
