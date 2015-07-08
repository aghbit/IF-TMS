name := """test"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.3.0",
  "org.webjars" % "bootstrap" % "2.3.1",
  "org.webjars" % "requirejs" % "2.1.11-1",
  "com.google.inject" % "guice" % "3.0",
  "javax.inject" % "javax.inject" % "1",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
  "junit" % "junit" % "4.10" % "test",
  "org.mockito" % "mockito-core" % "1.10.8",
  "org.mockito" % "mockito-all" % "1.10.8",
  "cglib" % "cglib" % "2.2",
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.1.6",
  "org.mongodb" % "casbah_2.11" % "2.8.1"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
