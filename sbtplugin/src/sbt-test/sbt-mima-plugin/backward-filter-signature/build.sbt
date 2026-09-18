import com.typesafe.tools.mima.core._

mimaPreviousArtifacts := Set(organization.value %% name.value % "0.0.1-SNAPSHOT")

// names the overload that stayed, so it matches nothing
mimaBinaryIssueFilters += ProblemFilters.exclude[DirectMissingMethodProblem]("A.f(Int)Int")
