import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info)
{
  val newReleaseToolsRepository = ScalaToolsSnapshots

  val mongoJavaDriver = "org.mongodb" % "mongo-java-driver" % "2.5.3"

  val specs2 = "org.specs2" %% "specs2" % "1.4-SNAPSHOT"

  var junit = "junit" % "junit" % "4.7"

  override def compileOptions = super.compileOptions ++ compileOptions("-Xexperimental")

 def specs2Framework = new TestFramework("org.specs2.runner.SpecsFramework")
 override def testFrameworks = super.testFrameworks ++ Seq(specs2Framework)

 val snapshots = "snapshots" at "http://scala-tools.org/repo-snapshots"
 val releases  = "releases" at "http://scala-tools.org/repo-releases"
 val sonatypeNexusSnapshots = "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

}
