addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.9.1")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.2.5")

addSbtPlugin("com.typesafe.sbt" % "sbt-s3" % "0.5")

resolvers += Resolver.url("sbt-plugin-releases",
  new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)
