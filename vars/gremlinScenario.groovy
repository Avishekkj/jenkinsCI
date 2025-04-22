// vars/gremlinScenario.groovy

def update(String scenarioId, Map params, String apiKey, String teamId) {
    def jsonPayload = """
    {
      "name": "Redundancy: Zone",
      "hypothesis": "This hypothesis will vary depending on your service and its use cases.",
      "description": "Test your service's availability...",
      "recommended_scenario_id": "zone-failure",
      "created_from_type": "Recommended",
      "created_from_id": "zone-failure",
      "graph": {
        "nodes": {
          "0": {
            "target_definition": {
              "containerSelection": { "selectionType": "ANY" },
              "strategy_type": "Random",
              "target_type": "Host",
              "strategy": {
                "percentage": 100,
                "multi_select_tags": {
                  "zone": ["${params.TARGET_ZONE}"]
                },
                "type": "RandomPercent"
              }
            },
            "impact_definition": {
              "infra_command_type": "blackhole",
              "infra_command_args": {
                "hostnames": "${params.HOSTNAME_PATTERN}",
                "egress_ports": "${params.EGRESS_PORTS}",
                "type": "blackhole",
                "length": ${params.ATTACK_LENGTH}
              }
            },
            "attack_configuration": {
              "command": {
                "infraCommandType": "blackhole",
                "infraCommandArgs": {
                  "hostnames": "${params.HOSTNAME_PATTERN}",
                  "egress_ports": "${params.EGRESS_PORTS}",
                  "length": ${params.ATTACK_LENGTH}
                }
              },
              "targets": [{
                "targetingStrategy": {
                  "type": "Host",
                  "multiSelectTags": {
                    "zone": ["${params.TARGET_ZONE}"]
                  }
                }
              }],
              "sampling": {
                "type": "Even",
                "percent": 100
              }
            },
            "include_new_targets": true,
            "type": "InfraAttack",
            "id": 0,
            "state": { "lifecycle": "NotStarted" }
          }
        },
        "start_id": "0"
      }
    }
    """.stripIndent()

    sh """
        curl -i -X PUT "https://api.gremlin.com/v1/scenarios/${scenarioId}?teamId=${teamId}" \
        -H "Content-Type: application/json" \
        -H "Authorization: Key ${apiKey}" \
        -d '${jsonPayload}'
    """
}

