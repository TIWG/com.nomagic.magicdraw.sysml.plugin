import sbt.Keys._
import sbt._

enablePlugins(AetherPlugin)

enablePlugins(GitVersioning)

scalaVersion := "2.11.8"

updateOptions := updateOptions.value.withCachedResolution(true)

PgpKeys.useGpg := true

PgpKeys.useGpgAgent := true

pgpSecretRing := file("local.secring.gpg")

pgpPublicRing := file("local.pubring.gpg")

// publish to bintray.com via: `sbt publish`
publishTo := Some(
  "TIWG" at
    s"https://api.bintray.com/content/tiwg/org.omg.tiwg.vendor.nomagic/com.nomagic.magicdraw.sysml.plugin/${version.value}")

val noSourcesSettings: Seq[Setting[_]] = Seq(

  publishArtifact in Compile := false,
  sources in Compile := Seq.empty,

  publishArtifact in Test := false,
  sources in Test := Seq.empty,

  managedSources := Seq.empty,

  organization := "org.omg.tiwg.vendor.nomagic",

  git.baseVersion := "18.0-sp6",

  // disable automatic dependency on the Scala library
  autoScalaLibrary := false,

  // disable using the Scala version in output paths and artifacts
  crossPaths := false,

  publishMavenStyle := true,

  // do not include all repositories in the POM
  pomAllRepositories := false,

  // make sure no repositories show up in the POM file
  pomIncludeRepository := { _ => false }
)

shellPrompt in ThisBuild := { state => Project.extract(state).currentRef.project + "> " }

lazy val core = Project("com_nomagic_magicdraw_sysml_plugin_upload", file("."))
  .settings(noSourcesSettings)
  .settings(
    name := "com.nomagic.magicdraw.sysml.plugin",
    moduleName := name.value,
    organization := "org.omg.tiwg.vendor.nomagic",

    libraryDependencies += 
      "org.omg.tiwg.vendor.nomagic"
        % "com.nomagic.magicdraw.package"
        % "18.0-sp6.2"
        artifacts
        Artifact("com.nomagic.magicdraw.package", "pom", "pom", None, Seq(), None, Map())
  )
  .settings(
    projectID := {
      val previous = projectID.value
      previous.extra("md.core" -> "http://api.nomagic.com/download_api/plugin/sysml/ltr/latest")
    }
  )


