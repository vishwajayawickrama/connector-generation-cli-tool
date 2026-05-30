package io.ballerina.aigentool;

import io.ballerina.aigentool.spi.AiGenWorkflow;
import io.ballerina.aigentool.spi.AiGenWorkflowProvider;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

final class AiGenWorkflowRegistry {

    private final Map<String, AiGenWorkflow> workflows;
    private final AiGenWorkflow defaultWorkflow;

    private AiGenWorkflowRegistry(Map<String, AiGenWorkflow> workflows, AiGenWorkflow defaultWorkflow) {
        this.workflows = workflows;
        this.defaultWorkflow = defaultWorkflow;
    }

    static AiGenWorkflowRegistry load() {
        Map<String, AiGenWorkflow> workflows = new LinkedHashMap<>();
        AiGenWorkflow defaultWorkflow = null;

        for (AiGenWorkflowProvider provider : ServiceLoader.load(AiGenWorkflowProvider.class)) {
            for (AiGenWorkflow workflow : provider.workflows()) {
                if (workflow.defaultWorkflow()) {
                    if (defaultWorkflow != null) {
                        throw new IllegalStateException("Multiple default aigen workflows are registered");
                    }
                    defaultWorkflow = workflow;
                    continue;
                }

                AiGenWorkflow previous = workflows.putIfAbsent(workflow.name(), workflow);
                if (previous != null) {
                    throw new IllegalStateException("Multiple aigen workflows are registered for: " + workflow.name());
                }
            }
        }

        if (defaultWorkflow == null) {
            throw new IllegalStateException("No default aigen workflow is registered");
        }

        return new AiGenWorkflowRegistry(Map.copyOf(workflows), defaultWorkflow);
    }

    AiGenWorkflow resolve(String workflowName) {
        if (workflowName == null || workflowName.isBlank() || "help".equals(workflowName)) {
            return defaultWorkflow;
        }
        return Optional.ofNullable(workflows.get(workflowName)).orElse(defaultWorkflow);
    }
}
