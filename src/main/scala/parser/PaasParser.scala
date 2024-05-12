package com.github.mmvpm
package parser

import parser.model.{Params, ServiceConfig, ServiceDeploy, ServiceMap}

import java.io.File
import java.nio.file.Paths

class PaasParser(rootPath: String, yamlParser: YamlParser) {

  def parseServiceMaps: Seq[ServiceMap] =
    findAllYamls(mapsDir).map(yamlParser.parseUnsafe[ServiceMap])

  def parseDeployConfigs: Seq[ServiceDeploy] =
    findAllYamls(deployDir).map(yamlParser.parseUnsafe[ServiceDeploy])

  def parseServiceConfig(deployConfig: ServiceDeploy): ServiceConfig =
    ServiceConfig(
      common = getParams(deployConfig.general) ++ deployConfig.commonParams.getOrElse(Params.empty),
      test = getParams(deployConfig.test),
      prod = getParams(deployConfig.prod)
    )

  // internal

  private val rootDir = new File(rootPath)
  private val mapsDir = Paths.get(rootDir.getPath, "maps").toFile
  private val deployDir = Paths.get(rootDir.getPath, "deploy").toFile
  private val confDir = Paths.get(rootDir.getPath, "conf").toFile

  private def findAllYamls(directory: File): Seq[File] =
    findAllFiles(directory).filter { file =>
      file.getPath.endsWith(".yml") || file.getPath.endsWith(".yaml")
    }

  private def findAllFiles(directory: File): Seq[File] = {
    val (directories, files) = directory.listFiles().partition(_.isDirectory)
    files ++ directories.flatMap(findAllFiles)
  }

  private def getParams(optLayer: Option[ServiceDeploy.Layer]): Params =
    (for {
      serviceLayer <- optLayer
      configSources <- serviceLayer.config
      allParams = getParams(configSources)
    } yield allParams).getOrElse(Params.empty)

  private def getParams(configSources: ServiceDeploy.ConfigSources): Params = {
    val fromParams = configSources.params.getOrElse(Params.empty)
    val fromFiles = configSources.files.map(getParamsFromFiles).getOrElse(Params.empty)
    fromParams ++ fromFiles
  }

  private def getParamsFromFiles(filenames: Seq[String]): Params =
    filenames
      .map(getParamsFromFile)
      .foldLeft(Params.empty)(_ ++ _)

  private def getParamsFromFile(filename: String): Params = {
    val fixedFilename = filename.stripPrefix("conf/")
    val configFile = Paths.get(confDir.getPath, fixedFilename).toFile
    val result = yamlParser.parseIgnoringEmpty[Params](configFile, Params.empty)

    result match {
      case Right(params) =>
        params
      case Left(error) =>
        println(s"Failed to parse $filename: $error")
        Params.empty
    }
  }
}
