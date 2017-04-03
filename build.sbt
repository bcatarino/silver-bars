name := """silver-bars"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,
  "org.specs2" %% "specs2-scalacheck" % "3.6" % "test",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)

