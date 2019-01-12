import mill._
import mill.scalalib._
import mill.modules.Jvm._
import mill.define.Task
import ammonite.ops._
import ammonite.ops.ImplicitWd._

import $file.^.`scala-wake`.common, common._

trait ChiselCommonModule extends SbtModule with CommonOptions {
  val macroPlugins = Agg(ivy"org.scalamacros:::paradise:2.1.0")
  def scalacPluginIvyDeps = macroPlugins
  def compileIvyDeps = macroPlugins

  def ivyDeps = Agg(
    ivy"com.typesafe.scala-logging::scala-logging:3.7.2",
    ivy"net.jcazevedo::moultingyaml:0.4.0"
  )
}

// Define the common chisel module.
trait Chisel3Base extends ChiselCommonModule with WakeModule with BuildInfo with SingleJar { outer =>
  def millSourcePath = os.pwd / up / 'chisel3

  def unmanagedJarPath = T { millSourcePath / up / "lib" }
  object coreMacros extends ChiselCommonModule {
    def unmanagedJarPath = outer.unmanagedJarPath
  }

  object chiselFrontend extends ChiselCommonModule {
    def unmanagedJarPath = outer.unmanagedJarPath
    def moduleDeps = outer.wakeModuleDeps ++ Seq(coreMacros)
  }

  override def moduleDeps: Seq[ScalaModule] = Seq(coreMacros, chiselFrontend)

  override def ivyDeps = super.ivyDeps() ++ Agg(
    ivy"com.github.scopt::scopt:3.6.0"
  )

  // This is required for building a library, but not for a `run` target.
  // In the latter case, mill will determine this on its own.
  //def mainClass = Some("chisel3.Driver")

  override def buildInfoMembers = T {
    Map[String, String](
      "buildInfoPackage" -> artifactName(),
      "version" -> "3.2-SNAPSHOT",
      "scalaVersion" -> scalaVersion()
    )
  }
}

