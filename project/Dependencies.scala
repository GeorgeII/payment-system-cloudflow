import sbt._

object Dependencies {

  lazy val all: Seq[ModuleID] = Seq(
    Cloudflow.Flink,
    Cloudflow.Akka,
    "com.lightbend.akka" %% "akka-stream-alpakka-file" % versions.alpakka,
    "com.typesafe.akka"  %% "akka-http-spray-json"     % versions.`akka-http`,
    "ch.qos.logback"     % "logback-classic"           % versions.logback,
    "com.typesafe.akka"  %% "akka-http-testkit"        % versions.`akka-http` % "test",
    "org.scalatest"      %% "scalatest"                % versions.scalatest   % "test"
  )

  object versions {
    val cloudflow   = "2.0.25"
    val alpakka     = "1.1.2"
    val `akka-http` = "10.1.12"
    val logback     = "1.2.3"
    val scalatest   = "3.0.8"
  }

  object Cloudflow {
    lazy val Flink = "com.lightbend.cloudflow" %% "cloudflow-flink" % versions.cloudflow
    lazy val Akka  = "com.lightbend.cloudflow" %% "cloudflow-akka"  % versions.cloudflow
  }
}
