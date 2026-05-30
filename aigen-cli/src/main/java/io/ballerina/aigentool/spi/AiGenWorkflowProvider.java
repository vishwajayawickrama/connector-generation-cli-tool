package io.ballerina.aigentool.spi;

import java.util.Collection;

public interface AiGenWorkflowProvider {

    Collection<AiGenWorkflow> workflows();
}
