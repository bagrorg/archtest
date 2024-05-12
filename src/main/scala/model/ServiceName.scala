package com.github.mmvpm
package model

final case class ServiceName(name: String) extends AnyVal {
  override def toString: String = name
}
