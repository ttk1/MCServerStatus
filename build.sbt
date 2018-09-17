name := """MCServerStatus"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.11"

libraryDependencies += javaJdbc
libraryDependencies += cache
libraryDependencies += javaWs

lazy val root = (project in file(".")).enablePlugins(PlayJava,BuildInfoPlugin).
  settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "net.ttk1.mcserverstatus"
  )