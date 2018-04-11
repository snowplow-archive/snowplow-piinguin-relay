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

import sbt._

object Dependencies {
  object V {
    val snowplowScalaAnalyticsSdkVersion =  "0.3.0"
    val piinguinClientVersion            =  "0.1.0"
    val awsLambdaJavaCoreVersion         =  "1.2.0"
    val awsLambdaJavaEventsVersion       =  "2.1.0"
    val amazonKinesisClientVersion       =  "1.9.0"
    val json4sVersion                    =  "3.5.4"
  }

  object Libraries {
    val snowplowScalaAnalyticsSdk  = "com.snowplowanalytics" %% "snowplow-scala-analytics-sdk"  % V.snowplowScalaAnalyticsSdkVersion
    val piinguinClient             = "com.snowplowanalytics" %% "piinguin-client"               % V.piinguinClientVersion
    val json4s                     = "org.json4s"            %% "json4s-jackson"                % V.json4sVersion
    val awsLambdaJavaCore          = "com.amazonaws"         % "aws-lambda-java-core"           % V.awsLambdaJavaCoreVersion
    val awsLambdaJavaEvents        = "com.amazonaws"         % "aws-lambda-java-events"         % V.awsLambdaJavaEventsVersion
    val amazonKinesisClient        = "com.amazonaws"         % "amazon-kinesis-client"          % V.amazonKinesisClientVersion
  }
}
