# Gremlin Shared Jenkins Library

This is a reusable Jenkins shared library for updating and triggering Gremlin chaos engineering scenarios via REST API.


### Sample Usage in Jenkinsfile

```groovy
@Library('gremlin-shared-lib') _

pipeline {
    agent any

    environment {
        GREMLIN_API_KEY = credentials('gremlinkey')
        GREMLIN_TEAM_ID = credentials('gremlinteamid')
        SCENARIO_ID = 'your-scenario-id'
    }

    parameters {
        string(name: 'TARGET_ZONE', defaultValue: 'ap-southeast-2c')
        string(name: 'HOSTNAME_PATTERN', defaultValue: '^api.gremlin.com')
        string(name: 'EGRESS_PORTS', defaultValue: '^53')
        string(name: 'ATTACK_LENGTH', defaultValue: '120')
    }

    stages {
        stage('Trigger Attack') {
            steps {
                script {
                    gremlinScenario.update(
                        "${env.SCENARIO_ID}",
                        [
                            TARGET_ZONE: params.TARGET_ZONE,
                            HOSTNAME_PATTERN: params.HOSTNAME_PATTERN,
                            EGRESS_PORTS: params.EGRESS_PORTS,
                            ATTACK_LENGTH: params.ATTACK_LENGTH
                        ],
                        env.GREMLIN_API_KEY,
                        env.GREMLIN_TEAM_ID
                    )
                }
            }
        }
    }
}

