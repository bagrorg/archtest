package com.github.mmvpm
package model

final case class Host(host: String) extends AnyVal {
  override def toString: String = host
}
