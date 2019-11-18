name := """jsi-server"""
organization := "com.saasquatch"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.1"

EclipseKeys.projectFlavor := EclipseProjectFlavor.Java
EclipseKeys.executionEnvironment := Some(EclipseExecutionEnvironment.JavaSE18)
EclipseKeys.preTasks := Seq(compile in Compile)
EclipseKeys.createSrc := EclipseCreateSrc.ValueSet(EclipseCreateSrc.ManagedClasses, EclipseCreateSrc.ManagedResources)
EclipseKeys.withJavadoc := true
EclipseKeys.withSource := true

resolvers ++= Seq(
  "bintray-saasquatch-java-libs" at "https://dl.bintray.com/saasquatch/java-libs"
)

libraryDependencies ++= Seq(
  guice,
  "com.saasquatch" % "json-schema-inferrer" % "0.1.1-alpha-29",
  "commons-validator" % "commons-validator" % "1.6"
)
