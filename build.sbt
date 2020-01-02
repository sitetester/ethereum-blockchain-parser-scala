
name := "ethereum-blockchain-parser-scala"

version := "0.1"

scalaVersion := "2.13.1"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.6.1"
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.11"
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.10"
libraryDependencies += "com.google.http-client" % "google-http-client-gson" % "latest.integration"