import com.typesafe.sbt.packager.archetypes.ServerLoader.Systemd
name := """Vachin"""

organization := "Vachin"

version := "1.0"

scalaVersion := "2.11.7"


lazy val root = (project in file(".")).enablePlugins(PlayScala, SbtWeb, DebianPlugin)

maintainer in Linux := "Kiran Sunkari<iamkiransunkari@gmail.com>"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-mailer" % "3.0.1",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.14",
  ws,
  filters
)

serverLoading in Debian := Systemd

routesGenerator := InjectedRoutesGenerator

resolvers ++= Seq(
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "RoundEights" at "http://maven.spikemark.net/roundeights"
)

sources in (Compile, doc) := Seq.empty

routesGenerator := InjectedRoutesGenerator

resolvers ++= Seq(
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
)

