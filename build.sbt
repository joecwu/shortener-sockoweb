name := "shortener-sockoweb"

version := "1.0"

scalaVersion := "2.11.7"

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.1.3"

libraryDependencies += "com.joecwu" %% "shortener" % "0.1.0"

libraryDependencies += "org.mashupbots.socko" %% "socko-webserver" % "0.6.0"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.8"

libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % "2.3.8"

libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.2.11"

libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.4.4"