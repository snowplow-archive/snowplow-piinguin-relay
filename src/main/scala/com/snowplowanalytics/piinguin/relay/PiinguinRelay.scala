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

package com.snowplowanalytics.piinguin.relay

// Scala
import scala.collection.JavaConverters._
import scala.concurrent.duration._
import scala.concurrent.{ Await, Future }

// Json4s
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.MappingException

// Kinesis
import com.amazonaws.services.lambda.runtime.events.KinesisEvent

// Piinguin Client
import com.snowplowanalytics.piinguin.client.PiinguinClient

// Analytics SDK
import com.snowplowanalytics.snowplow.analytics.scalasdk.json.EventTransformer

class PiinguinRelay {
  import scala.concurrent.ExecutionContext.Implicits.global
  implicit val formats = DefaultFormats
  private val piinguinTimeout = sys.env("PIINGUIN_TIMEOUT_SEC").toInt
  private val piinguinHost = sys.env("PIINGUIN_HOST")
  private val piinguinPort = sys.env("PIINGUIN_PORT").toInt
  private val piinguinClient = new PiinguinClient(piinguinHost, piinguinPort)

  /**
   * AWS Lambda handler implementation
   */
  def recordHandler(event: KinesisEvent) {
    val events: List[Either[List[String], List[Future[Either[String, String]]]]] = for {
      rec <- event.getRecords.asScala.toList
      line = new String(rec.getKinesis.getData.array(), "UTF-8")
      validated = EventTransformer.jsonifyGoodEvent(line.split("\t", -1))
      kvPairs = validated.map {
        case (_, json) =>
          extractKVPairs(json).map { case (mf: ModifiedField) => piinguinClient.createPiiRecord(mf.modifiedValue, mf.originalValue) }
      }
    } yield kvPairs
    val (errors, futures) = events.partition(_.isLeft)
    errors.collect { case Left(l) => l }.foreach { case (errList: List[String]) => println(errList.mkString("\n")) } // Send to CW
    val results = Await.result(Future.sequence(futures.map(_.right.get).flatten), piinguinTimeout seconds)
    val (errorList, successList) = results.partition(_.isLeft)
    successList.collect { case Right(r) => r }.foreach(println(_)) // Send to CW
    errorList.collect {case Left(l) => l }.foreach(println(_)) // Send to CW
    if (errorList.nonEmpty) throw new Exception(s"Errors encountered ${errorList.mkString("\n")}")
  }

  private def extractKVPairs(json: JObject): List[ModifiedField] =
    List("pojo", "json").map(extractValues(_,
      (json \ "unstruct_event_com_snowplowanalytics_snowplow_pii_transformation_1" \ "pii"))).flatten // Letting this throw (pii event should be validated)

  private def extractValues(key: String, pii: JValue): List[ModifiedField] =
    (pii \ key).extractOpt[List[ModifiedField]].getOrElse(List.empty[ModifiedField])

}

final case class ModifiedField(originalValue: String, modifiedValue: String)
