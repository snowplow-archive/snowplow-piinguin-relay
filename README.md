# Snowplow Piinguin Relay

[![Build Status][travis-image]][travis]
[![Release][release-image]][release]
[![License][license-image]][license]

Pushes PII events to Piinguin using the piinguin client.

This relay is meant to listen to EnrichedEvent formatted messages coming on an AWS Kinesis stream containing all the extracted PII from Snowplow. It then forwards those to a `piinguin-server`. Please read the piinguin project documentation for more information [here][piinguin].

## Deployment

You can always use the published version [here][s3-assets].

If you need to rebuild and deploy yourself then follow those steps:

* `sbt assembly`
* Copy artifact to S3 (it's too large for the console) by e.g. `aws s3 cp target/scala-2.12/assembled_jars/piinguin-relay-0.1.0.jar s3://somebucket/`

## Configuration

* Ensure that you are running this lambda in the same VPC and subnet as the piinguin server
* Ensure that the lambda is in a security group that is allowed to talk to the piinguin server
* Create a trigger from the kinesis stream that has the PII events coming from stream enrich
* Set the environment variables required by the lambda in the AWS console or the cli e.g.:

```bash
PIINGUIN_HOST = ec2-1-2-3-4.eu-west-1.compute.amazonaws.com
PIINGUIN_PORT = 8080
PIINGUIN_TIMEOUT_SEC = 10 (should be lower than the lambda timeout)
```

* Use the `Java 8` runtime and:
* * Handler should be: `com.snowplowanalytics.piinguin.relay.PiinguinRelay::recordHandler`
* * S3 URL link should be something like (depending on where you uploaded or one of the [standard locations][s3-assets]): `https://s3-eu-west-1.amazonaws.com/somebucket/piinguin-relay-assembly-0.1.0.jar`

## Copyright and license

Copyright 2018 Snowplow Analytics Ltd.

Licensed under the [Apache License, Version 2.0][license] (the "License");
you may not use this software except in compliance with the License.

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

[license]: http://www.apache.org/licenses/LICENSE-2.0
[piinguin]: https://github.com/snowplow-incubator/piinguin
[s3-assets]: https://github.com/snowplow/snowplow/wiki/Hosted-assets

[travis-image]: https://travis-ci.org/snowplow-incubator/snowplow-piinguin-relay.svg?branch=master
[travis]: https://travis-ci.org/snowplow-incubator/snowplow-piinguin-relay

[release-image]: https://img.shields.io/badge/release-0.1.0-orange.svg?style=flat
[release]: https://github.com/snowplow-incubator/snowplow-piinguin-relay/releases

[license-image]: http://img.shields.io/badge/license-Apache--2-blue.svg?style=flat
[license]: http://www.apache.org/licenses/LICENSE-2.0
