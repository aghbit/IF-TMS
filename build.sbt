name := """test"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.3.0",
  "org.webjars" % "bootstrap" % "2.3.1",
  "org.webjars" % "requirejs" % "2.1.11-1",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23",
  "com.google.inject" % "guice" % "3.0",
  "javax.inject" % "javax.inject" % "1",
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
  "junit" % "junit" % "4.10" % "test",
  "org.mockito" % "mockito-core" % "1.10.8",
  "org.mockito" % "mockito-all" % "1.10.8",
  "org.springframework" % "spring-core" % "4.0.7.RELEASE",
  "org.springframework" % "spring-context" % "4.0.7.RELEASE",
  "org.springframework.data" % "spring-data-mongodb" % "1.6.1.RELEASE",
  "cglib" % "cglib" % "2.2",
  "org.mongodb" % "casbah_2.11" % "2.8.1"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
