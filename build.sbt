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

version := "0.1.0-rc2"
organization := "com.snowplowanalytics"
scalaVersion := "2.12.6"
name := "piinguin-relay"

// Dependencies
libraryDependencies ++= Seq(
  "com.snowplowanalytics" %% "snowplow-scala-analytics-sdk" % "0.3.0",
  "com.snowplowanalytics" %% "piinguin-client" % "0.1.0-M4",
  "com.amazonaws" % "aws-lambda-java-core" % "1.2.0",
  "com.amazonaws" % "aws-lambda-java-events" % "2.1.0",
  "com.amazonaws" % "amazon-kinesis-client" % "1.9.0"
)

assemblyMergeStrategy in assembly := {
      case PathList(ps @ _*) if ps.last endsWith "io.netty.versions.properties" => MergeStrategy.first
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
