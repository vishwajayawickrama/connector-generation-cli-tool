package io.ballerina.aigentool.spi;

import io.ballerina.runtime.api.Module;

import java.util.Objects;

public final class AiGenWorkflow {

    private final String name;
    private final String org;
    private final String moduleName;
    private final String version;
    private final boolean defaultWorkflow;

    public AiGenWorkflow(String name, String org, String moduleName, String version, boolean defaultWorkflow) {
        this.name = requireNonBlank(name, "name");
        this.org = requireNonBlank(org, "org");
        this.moduleName = requireNonBlank(moduleName, "moduleName");
        this.version = requireNonBlank(version, "version");
        this.defaultWorkflow = defaultWorkflow;
    }

    public String name() {
        return name;
    }

    public boolean defaultWorkflow() {
        return defaultWorkflow;
    }

    public Module module() {
        return new Module(org, moduleName, version);
    }

    private static String requireNonBlank(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be blank");
        }
        return value;
    }
}
