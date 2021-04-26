//tag::get-started[]
//tag::local-conf[]
lazy val paymentSystem =  (project in file("."))
  .enablePlugins(CloudflowApplicationPlugin)
  .settings(
    scalaVersion := "2.12.10",
    runLocalConfigFile := Some("src/main/resources/local.conf"), //<1>
//    runLocalLog4jConfigFile := Some("src/main/resources/log4j.xml"), //<2>
    name := "payment-system-cloudflow",
    //end::local-conf[]

    libraryDependencies ++= Dependencies.all
  )
  //end::get-started[]
  .enablePlugins(ScalafmtPlugin)
  .settings(
    scalafmtOnCompile := true,

    crossScalaVersions := Vector(scalaVersion.value),
    scalacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-target:jvm-1.8",
      "-Xlog-reflective-calls",
      "-Xlint",
      "-Ywarn-unused",
      "-deprecation",
      "-feature",
      "-language:_",
      "-unchecked"
    ),

    scalacOptions in (Compile, console) --= Seq("-Ywarn-unused"),
    scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value
  )

dynverSeparator in ThisBuild := "-"
