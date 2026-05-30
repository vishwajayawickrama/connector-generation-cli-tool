package io.ballerina.aigentool;

import java.util.ArrayList;
import java.util.List;

import io.ballerina.aigentool.spi.AiGenWorkflow;
import io.ballerina.cli.BLauncherCmd;
import io.ballerina.runtime.api.Module;
import io.ballerina.runtime.api.Runtime;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BError;
import picocli.CommandLine;

@CommandLine.Command(
        name = "aigen",
        description = "Centralized CLI tool to generate and maintain Ballerina connector assets with AI assistance.",
        mixinStandardHelpOptions = true
)
public class AiGenCmd implements BLauncherCmd {

    private static final String COMMAND_NAME = "aigen";

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @CommandLine.Parameters(index = "0..*", arity = "0..*", description = "Connector automator arguments")
    private final List<String> args = new ArrayList<>();

    @Override
    public void execute() {
        Runtime runtime = null;
        boolean runtimeStarted = false;
        try {
            AiGenWorkflow workflow = AiGenWorkflowRegistry.load().resolve(args.isEmpty() ? "" : args.get(0));
            Module module = workflow.module();
            BArray ballerinaArgs = StringUtils.fromStringArray(args.toArray(String[]::new));

            runtime = Runtime.from(module);
            runtime.init();
            runtime.start();
            runtimeStarted = true;

            Object result = runtime.callFunction(module, "main", null, ballerinaArgs);
            if (result instanceof BError error) {
                System.err.println("Error occurred while running connector automator: " + error.getErrorMessage());
            }
        } catch (Exception e) {
            System.err.println("Error occurred while initializing the runtime: " + e.getMessage());
        } finally {
            if (runtimeStarted && runtime != null) {
                runtime.stop();
            }
        }
    }

    @Override
    public String getName() {
        return COMMAND_NAME;
    }

    @Override
    public void printLongDesc(StringBuilder out) {
        out.append("Generate and maintain Ballerina connector assets from OpenAPI specifications or Java SDKs.");
    }

    @Override
    @SuppressWarnings("deprecation")
    public void printUsage(StringBuilder out) {
        out.append("bal aigen <sdk|openapi> <command> [args...]");
    }

    @Override
    public void setParentCmdParser(CommandLine parentCmdParser) {
    }
}
