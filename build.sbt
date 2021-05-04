scalaVersion in ThisBuild := "2.12.10"

val appName = "payment-system-cloudflow"

lazy val root = Project(id = appName, base = file("."))
  .settings(
    name := appName
  )
  .aggregate(
    paymentIngress,
    paymentChecker,
    participantInitializer,
    paymentLogger,
    paymentProcessor
  )


//lazy val global = project
//  .aggregate(
//    paymentIngress,
//    paymentChecker,
//    participantInitializer,
//    paymentLogger,
//    paymentProcessor
//  )
//  .enablePlugins(CloudflowApplicationPlugin)
//  .settings(
//    scalaVersion := "2.12.10",
//    runLocalConfigFile := Some("src/main/resources/local.conf"), //<1>
//    //    runLocalLog4jConfigFile := Some("src/main/resources/log4j.xml"), //<2>
//    name := "payment-system-cloudflow",
//    //end::local-conf[]
//
//    libraryDependencies ++= Dependencies.all
//  )
//  //end::get-started[]
//  .enablePlugins(ScalafmtPlugin)
//  .settings(
//    scalafmtOnCompile := true,
//
//    crossScalaVersions := Vector(scalaVersion.value),
//    scalacOptions ++= Seq(
//      "-encoding", "UTF-8",
//      "-target:jvm-1.8",
//      "-Xlog-reflective-calls",
//      "-Xlint",
//      "-Ywarn-unused",
//      "-deprecation",
//      "-feature",
//      "-language:_",
//      "-unchecked"
//    ),
//
//    scalacOptions in (Compile, console) --= Seq("-Ywarn-unused"),
//    scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value
//  )

dynverSeparator in ThisBuild := "-"


lazy val paymentIngress = appModule("payment-ingress")
  .enablePlugins(CloudflowAkkaPlugin)
  .settings(
    libraryDependencies ++= Dependencies.all
  )
  .dependsOn()

//  .dependsOn(
//    paymentChecker,
//  )

lazy val paymentChecker = appModule("payment-checker")
  .enablePlugins(CloudflowFlinkPlugin)
  .settings(
    libraryDependencies ++= Dependencies.all
  )
//  .dependsOn(
//    paymentIngress,
//    participantInitializer,
//    paymentLogger,
//    paymentProcessor
//  )

lazy val participantInitializer = appModule("payment-initializer")
  .enablePlugins(CloudflowApplicationPlugin, CloudflowAkkaPlugin)
  .settings(
    libraryDependencies ++= Dependencies.all
  )
//  .dependsOn(
//    paymentIngress,
//    paymentChecker,
//    paymentLogger,
//    paymentProcessor
//  )

lazy val paymentLogger = appModule("payment-logger")
  .enablePlugins(CloudflowAkkaPlugin)
  .settings(
    libraryDependencies ++= Dependencies.all
  )
//  .dependsOn(
//    paymentIngress,
//    paymentChecker,
//    participantInitializer,
//    paymentProcessor
//  )

lazy val paymentProcessor = appModule("payment-processor")
  .enablePlugins(CloudflowFlinkPlugin)
  .settings(
    libraryDependencies ++= Dependencies.all
  )
//  .dependsOn(
//    paymentIngress,
//    paymentChecker,
//    participantInitializer,
//    paymentLogger,
//  )


def appModule(moduleID: String): Project = {
  Project(id = moduleID, base = file(moduleID))
    .settings(name := moduleID)
    .settings(
      Settings.commonSettings
    )
}






//tag::get-started[]
//tag::local-conf[]
//lazy val paymentSystem =  (project in file("."))
//  .enablePlugins(CloudflowApplicationPlugin)
//  .settings(
//    scalaVersion := "2.12.10",
//    runLocalConfigFile := Some("src/main/resources/local.conf"), //<1>
////    runLocalLog4jConfigFile := Some("src/main/resources/log4j.xml"), //<2>
//    name := "payment-system-cloudflow",
//    //end::local-conf[]
//
//    libraryDependencies ++= Dependencies.all
//  )
//  //end::get-started[]
//  .enablePlugins(ScalafmtPlugin)
//  .settings(
//    scalafmtOnCompile := true,
//
//    crossScalaVersions := Vector(scalaVersion.value),
//    scalacOptions ++= Seq(
//      "-encoding", "UTF-8",
//      "-target:jvm-1.8",
//      "-Xlog-reflective-calls",
//      "-Xlint",
//      "-Ywarn-unused",
//      "-deprecation",
//      "-feature",
//      "-language:_",
//      "-unchecked"
//    ),
//
//    scalacOptions in (Compile, console) --= Seq("-Ywarn-unused"),
//    scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value
//  )
//
//dynverSeparator in ThisBuild := "-"
