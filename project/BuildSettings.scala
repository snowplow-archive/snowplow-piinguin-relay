/*
 * Copyright (c) 2018 Snowplow Analytics Ltd. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */

// SBT
import sbt._
import Keys._

// Assembly
import sbtassembly.AssemblyPlugin.autoImport._

object BuildSettings {

  lazy val buildSettings = basicSettings ++ assemblySettings ++ dependencies

  lazy val dependencies = Seq(
    libraryDependencies ++= Seq(
      Dependencies.Libraries.snowplowScalaAnalyticsSdk,
      Dependencies.Libraries.piinguinClient,
      Dependencies.Libraries.awsLambdaJavaCore,
      Dependencies.Libraries.awsLambdaJavaEvents,
      Dependencies.Libraries.amazonKinesisClient
    )
  )

  lazy val basicSettings = Seq(
    version := "0.1.0-rc1",
    organization := "com.snowplowanalytics",
    scalaVersion := "2.12.6",
    name := "snowplow-piinguin-relay"
  )

  lazy val assemblySettings = Seq(
    assembly / target := file("target/scala-2.12/assembled_jars/"),
    assembly / assemblyJarName := { name.value + "-" + version.value + ".jar" },
    assembly / assemblyMergeStrategy := {
      case PathList(ps @ _*) if ps.last endsWith "io.netty.versions.properties" => MergeStrategy.first
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  )
}
