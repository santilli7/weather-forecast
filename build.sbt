name := "weather_forecast"

version := "1.0"

scalaVersion := "2.10.7"
val sparkVersion = "2.0.0"
assemblyJarName in assembly := "weather-forecast.jar"

parallelExecution in Test := false

libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-hive" % sparkVersion

libraryDependencies ++= Seq(
                            "com.cloudera.sparkts" % "sparkts" % "0.1.0",
                              "org.scalatest" % "scalatest_2.10" % "2.2.6" % "test" withSources() withJavadoc(),
                              "joda-time" % "joda-time" % "2.9.2" withSources() withJavadoc(),
                              "org.json4s" %% "json4s-native" % "3.6.7"
                              )
/*
if a project uses a particular version of scala, but a transitive dependency uses one of the scala-lang optionals
(compiler, scalap, reflect, etc) then the older version is used. It would be good if something like this was automatic

In Spark, spark core has dependency on scala lang, so they can be overriden as below

#Ref - https://github.com/sbt/sbt/issues/2286
*/

dependencyOverrides ++= Seq("org.scala-lang" % "scala-compiler" % scalaVersion.value,
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.scala-lang" % "scala-library" % scalaVersion.value)

/*
When transitive dependencies point to same jar, below merge strategy will help choose one of the jars
from the duplicates

Ref - http://stackoverflow.com/questions/30446984/spark-sbt-assembly-deduplicate-different-file-contents-found-in-the-followi/31618903
https://github.com/sbt/sbt-assembly
*/
assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs@_*) => MergeStrategy.last
  case PathList("javax", "activation", xs@_*) => MergeStrategy.last
  case PathList("org", "apache", xs@_*) => MergeStrategy.last
  case PathList("com", "google", xs@_*) => MergeStrategy.last
  case PathList("com", "esotericsoftware", xs@_*) => MergeStrategy.last
  case PathList("com", "twitter", xs@_*) => MergeStrategy.last
  case PathList("com", "codahale", xs@_*) => MergeStrategy.last
  case PathList("com", "yammer", xs@_*) => MergeStrategy.last
  case "about.html" => MergeStrategy.rename
  case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
  case "META-INF/mailcap" => MergeStrategy.last
  case "META-INF/mimetypes.default" => MergeStrategy.last
  case "plugin.properties" => MergeStrategy.last
  case "log4j.properties" => MergeStrategy.last
  case "unwanted.txt" => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

assemblyExcludedJars in assembly := {
  val cp = (fullClasspath in assembly).value
  val excludesJar = Set("commons-beanutils-1.8.0.jar", "commons-beanutils-core-1.8.0.jar",
                            "stax-api-1.0.1.jar")
  cp filter { jar => excludesJar.contains(jar.data.getName)}
}

assemblyMergeStrategy in assembly := {
 case PathList("META-INF", xs @ _*) => MergeStrategy.discard
 case x => MergeStrategy.first
}


resolvers ++= Seq(
  "JBoss Repository" at "http://repository.jboss.org/nexus/content/repositories/releases/",
  "Spray Repository" at "http://repo.spray.cc/",
  "Cloudera Repository" at "https://repository.cloudera.com/artifactory/cloudera-repos/",
  "Akka Repository" at "http://repo.akka.io/releases/",
  "Twitter4J Repository" at "http://twitter4j.org/maven2/",
  "Apache HBase" at "https://repository.apache.org/content/repositories/releases",
  "Twitter Maven Repo" at "http://maven.twttr.com/",
  "scala-tools" at "https://oss.sonatype.org/content/groups/scala-tools",
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Second Typesafe repo" at "http://repo.typesafe.com/typesafe/maven-releases/",
  "Third Typsafe repo" at "https://repo.typesafe.com/typesafe/ivy-releases/",
  "Mesosphere Public Repository" at "http://downloads.mesosphere.io/maven",
  "artifactory" at "http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases",
  Resolver.sonatypeRepo("public")
)


