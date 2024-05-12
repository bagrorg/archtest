ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.13"

libraryDependencies += "io.circe" %% "circe-yaml"           % "1.15.0"
libraryDependencies += "io.circe" %% "circe-core"           % "0.14.6"
libraryDependencies += "io.circe" %% "circe-generic"        % "0.14.6"
libraryDependencies += "io.circe" %% "circe-parser"         % "0.14.6"
libraryDependencies += "io.circe" %% "circe-generic-extras" % "0.14.3"


libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.18" % Test

lazy val root = (project in file("."))
  .settings(
    name := "archtest",
    idePackagePrefix := Some("com.github.mmvpm")
  )
