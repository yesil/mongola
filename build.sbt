name := "mongola"

version := "0.1-SNAPSHOT"

scalaVersion := "2.9.0-1"

checksums := Nil

scalacOptions += "-Xexperimental"

testFrameworks ++= Seq(new TestFramework("org.specs2.runner.SpecsFramework"))

externalIvyFile( baseDirectory { base => base / "ivy.xml"} )

externalIvySettings()
