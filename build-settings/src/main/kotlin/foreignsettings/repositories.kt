package foreignsettings

import org.gradle.api.artifacts.dsl.*
import java.net.*

fun RepositoryHandler.remoteDistribution(url: String, subgroup: String, artifact: String) {
    exclusiveContent {
        filter {
            includeGroup("foreignbuild.$subgroup")
        }
        forRepository {
            ivy {
                this.url = URI(url)
                name = "$subgroup distributions"
                metadataSources { artifact() }
                patternLayout { artifact(artifact) }
            }
        }
    }
}
