package dev.httpmarco.polocloud.base;

import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.dependencies.Dependency;
import dev.httpmarco.polocloud.api.groups.CloudGroupService;
import dev.httpmarco.polocloud.api.node.NodeService;
import dev.httpmarco.polocloud.base.groups.CloudServiceGroupService;
import dev.httpmarco.polocloud.base.logging.FileLoggerHandler;
import dev.httpmarco.polocloud.base.logging.LoggerOutPutStream;
import dev.httpmarco.polocloud.base.node.CloudNodeService;
import dev.httpmarco.polocloud.base.terminal.CloudTerminal;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

@Getter
@Accessors(fluent = true)
public final class CloudBase extends CloudAPI {

    private final CloudTerminal terminal;
    private final NodeService nodeService;
    private final CloudGroupService groupService;

    private boolean running = true;

    public CloudBase() {
        dependencyService().load(new Dependency("dev.httpmarco", "osgan-utils", "1.1.15-SNAPSHOT", "1.1.15-20240514.180642-1", Dependency.MAVEN_CENTRAL_SNAPSHOT_REPO));
        dependencyService().load(new Dependency("dev.httpmarco", "osgan-files", "1.1.15-SNAPSHOT", "1.1.15-20240514.180642-1", Dependency.MAVEN_CENTRAL_SNAPSHOT_REPO));
        dependencyService().load(new Dependency("dev.httpmarco", "osgan-netty", "1.1.15-SNAPSHOT", "1.1.15-20240514.180642-1", Dependency.MAVEN_CENTRAL_SNAPSHOT_REPO));

        dependencyService().load(new Dependency("io.netty", "netty5-common", "5.0.0.Alpha5"));
        dependencyService().load(new Dependency("io.netty", "netty5-transport", "5.0.0.Alpha5"));
        dependencyService().load(new Dependency("io.netty", "netty5-resolver", "5.0.0.Alpha5"));
        dependencyService().load(new Dependency("io.netty", "netty5-transport-classes-epoll", "5.0.0.Alpha5"));


        dependencyService().load(new Dependency("com.google.code.gson", "gson", "2.10.1"));

        dependencyService().load(new Dependency("org.jline", "jline", "3.26.1"));
        dependencyService().load(new Dependency("org.fusesource.jansi", "jansi", "2.4.1"));

        this.terminal = new CloudTerminal();
        // register logging layers (for general output)
        this.loggerFactory().registerLoggers(new FileLoggerHandler(), terminal);

        System.setErr(new PrintStream(new LoggerOutPutStream(true), true, StandardCharsets.UTF_8));
        System.setOut(new PrintStream(new LoggerOutPutStream(), true, StandardCharsets.UTF_8));

        this.nodeService = new CloudNodeService();

        // print cloud header informations
        terminal.spacer();
        terminal.spacer("   &3PoloCloud &2- &1Simple minecraft cloudsystem &2- &1v1.0.12");
        terminal.spacer("   &1node&2: &1" + nodeService.localNode().name() + " &2| &1id&2: &1" + nodeService.localNode().id());
        terminal.spacer();

        this.nodeService.localNode().initialize();
        this.groupService = new CloudServiceGroupService();

        logger().info("Successfully started up!");
        this.terminal.start();
    }

    public void shutdown() {
        if (!running) {
            return;
        }
        running = false;

        logger().info("Shutdown cloud...");

        this.nodeService.localNode().close();
        logger().info("Networking was sucessfully closed");

        this.terminal.close();
        System.exit(0);
    }

    public static CloudBase instance() {
        return (CloudBase) CloudAPI.instance();
    }
}