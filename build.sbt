name := "learn-cats-parse"

version := "0.1"

scalaVersion := "3.0.2"

// те же версии доступны для scala 2.12
libraryDependencies += "org.typelevel" %% "cats-core"  % "2.6.1"
libraryDependencies += "org.typelevel" %% "cats-parse" % "0.3.4"

libraryDependencies += "org.typelevel" %% "jawn-parser" % "1.2.0"
libraryDependencies += "org.typelevel" %% "jawn-ast"    % "1.2.0"

libraryDependencies += "io.circe" %% "circe-parser" % "0.14.1"

// https://mvnrepository.com/artifact/org.scalatest/scalatest
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % Test
