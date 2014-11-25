name := """test"""

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.3.0",
  "org.webjars" % "bootstrap" % "2.3.1",
  "org.webjars" % "requirejs" % "2.1.11-1",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23",
  "com.google.inject" % "guice" % "3.0",
  "javax.inject" % "javax.inject" % "1"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
