organization := "org.swot"

name := "Swot"

version := "0.1"

scalaVersion := "2.9.2"

resolvers += "Sonatype" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies +=  "org.specs2" %% "specs2" % "1.12.1" % "test"

libraryDependencies += "org.scala-lang" % "scala-swing" % "2.9.2" 

libraryDependencies += "net.liftweb" % "lift-json_2.9.1" % "2.5-SNAPSHOT"
