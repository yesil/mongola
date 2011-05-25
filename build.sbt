name := "mongola"

version := "0.1-SNAPSHOT"

scalaVersion := "2.9.0"

checksums := Nil

scalacOptions += "-Xexperimental"

testFrameworks ++= Seq(new TestFramework("org.specs2.runner.SpecsFramework"))

libraryDependencies ++= Seq(
	"org.specs2" % "specs2_2.9.0" % "1.3",
	"org.mongodb" % "mongo-java-driver" % "2.5.3",
	"junit" % "junit" % "4.7"
)
