version := "0.1"

scalaVersion := "2.12.7"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.6.1"
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.11"
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.10"
libraryDependencies += "com.google.http-client" % "google-http-client-gson" % "latest.integration"

libraryDependencies ++= List(
  "com.typesafe.slick" %% "slick" % "3.3.0",
  "org.slf4j" % "slf4j-nop" % "1.7.26",
  "org.scalatest" %% "scalatest" % "3.0.8" % Test,
  "org.xerial" % "sqlite-jdbc" % "3.7.2"
)

scalacOptions += "-deprecation"
fork in run := true
