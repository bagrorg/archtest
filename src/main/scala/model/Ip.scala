package com.github.mmvpm
package model

final case class Ip(ip: String) extends AnyVal {
  override def toString: String = ip
}
