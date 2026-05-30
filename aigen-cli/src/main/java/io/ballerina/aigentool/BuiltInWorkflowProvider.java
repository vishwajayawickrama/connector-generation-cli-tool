package io.ballerina.aigentool;

import io.ballerina.aigentool.spi.AiGenWorkflow;
import io.ballerina.aigentool.spi.AiGenWorkflowProvider;

import java.util.Collection;
import java.util.List;

public final class BuiltInWorkflowProvider implements AiGenWorkflowProvider {

    private static final String ORG = "wso2";
    private static final String MODULE = "connector_automator";
    private static final String VERSION = "0";

    @Override
    public Collection<AiGenWorkflow> workflows() {
        return List.of(
                new AiGenWorkflow("sdk", ORG, MODULE, VERSION, false),
                new AiGenWorkflow("openapi", ORG, MODULE, VERSION, false),
                new AiGenWorkflow("default", ORG, MODULE, VERSION, true)
        );
    }
}
