import sbt._

object Dependencies {

  lazy val all: Seq[ModuleID] = common ++ akkaStreamlet ++ flinkStreamlet

  lazy val common: Seq[ModuleID] = Seq(
    "ch.qos.logback" % "logback-classic" % versions.logback,
    "org.scalatest"  %% "scalatest"      % versions.scalatest   % "test"
  )

  lazy val akkaStreamlet: Seq[ModuleID] = Seq(
//    Cloudflow.Akka,
    "com.lightbend.akka" %% "akka-stream-alpakka-file" % versions.alpakka,
    "com.typesafe.akka"  %% "akka-http-spray-json"     % versions.`akka-http`,
    "com.typesafe.akka"  %% "akka-protobuf"            % versions.akka,
    "com.typesafe.akka"  %% "akka-http-testkit"        % versions.`akka-http` % "test"
  )

  lazy val flinkStreamlet: Seq[ModuleID] = Seq(
//    Cloudflow.Flink
  )

  object Cloudflow {
//    lazy val Flink = "com.lightbend.cloudflow" %% "cloudflow-flink" % versions.cloudflow
//    lazy val Akka  = "com.lightbend.cloudflow" %% "cloudflow-akka"  % versions.cloudflow
  }

  object versions {
    val cloudflow   = "2.0.18"
    val akka        = "2.6.10"
    val alpakka     = "1.1.2"
    val `akka-http` = "10.1.12"
    val logback     = "1.2.3"
    val scalatest   = "3.0.8"
  }
}
