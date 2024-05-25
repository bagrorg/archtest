ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.13"

val CirceVersion = "0.14.6"
val CirceExtrasVersion = "0.14.3"
val CirceYamlVersion = "1.15.0"
val ScalatestVersion = "3.2.18"

val circe = Seq(
  "io.circe" %% "circe-core"           % CirceVersion,
  "io.circe" %% "circe-generic"        % CirceVersion,
  "io.circe" %% "circe-parser"         % CirceVersion,
  "io.circe" %% "circe-generic-extras" % CirceExtrasVersion,
  "io.circe" %% "circe-yaml"           % CirceYamlVersion,
)

val scalatest = Seq(
  "org.scalatest" %% "scalatest" % ScalatestVersion % Test
)

lazy val core = (project in file("core"))
  .settings(
    name := "core",
    idePackagePrefix := Some("com.github.mmvpm.core")
  )

lazy val parserShiva = (project in file("parsers/parser-shiva"))
  .dependsOn(core)
  .settings(
    name := "parser-shiva",
    idePackagePrefix := Some("com.github.mmvpm.parsers.shiva"),
    libraryDependencies ++= circe
  )

lazy val exampleShiva = (project in file("examples/example-shiva"))
  .dependsOn(core, parserShiva)
  .settings(
    name := "example-shiva",
    idePackagePrefix := Some("com.github.mmvpm.examples.shiva"),
    libraryDependencies ++= scalatest
  )

lazy val root = (project in file("."))
  .settings(
    name := "archtest"
  )
  .aggregate(
    core,
    parserShiva,
    exampleShiva
  )
