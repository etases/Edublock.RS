package io.github.etases.edublock.rs;

import io.github.etases.edublock.rs.api.ServerHandler;
import io.github.etases.edublock.rs.config.MainConfig;
import io.github.etases.edublock.rs.handler.CommandHandler;
import io.github.etases.edublock.rs.handler.HelloHandler;
import io.github.etases.edublock.rs.handler.JwtHandler;
import io.github.etases.edublock.rs.handler.SwaggerHandler;
import io.github.etases.edublock.rs.terminal.ServerTerminal;
import io.javalin.Javalin;
import lombok.Getter;
import me.hsgamer.hscore.config.proxy.ConfigGenerator;
import me.hsgamer.hscore.config.simpleconfiguration.SimpleConfig;
import org.simpleyaml.configuration.file.YamlFile;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Getter
public class RequestServer {
    private final CommandManager commandManager;
    private final ServerBuilder serverBuilder;
    private final MainConfig mainConfig;
    private final DependencyManager dependencyManager;
    private final ServerTerminal terminal;
    private Javalin server;

    private RequestServer() {
        mainConfig = ConfigGenerator.newInstance(MainConfig.class, new SimpleConfig<>(new File(".", "config.yml"), new YamlFile(), (file, yamlFile) -> {
            yamlFile.setConfigurationFile(file);
            try {
                yamlFile.loadWithComments();
            } catch (IOException e) {
                Logger.warn(e);
            }
        }));
        commandManager = new CommandManager();
        serverBuilder = new ServerBuilder();
        dependencyManager = new DependencyManager(this);
        terminal = dependencyManager.getInjector().getInstance(ServerTerminal.class);
    }

    public static void main(String[] args) {
        new RequestServer().start();
    }

    public void start() {
        try {
            terminal.init();
        } catch (Exception e) {
            Logger.error("Failed to initialize server terminal", e);
            return;
        }

        getHandlers().forEach(clazz -> dependencyManager.getInjector().getInstance(clazz).setup());

        server = serverBuilder.build();
        server.start(7070);
        terminal.start();
    }

    public void stop() {
        if (server != null) {
            server.stop();
        }
        terminal.shutdown();
        commandManager.disable();
    }

    /**
     * Get the list of handlers to use in the server
     *
     * @return the list of handlers
     */
    private List<Class<? extends ServerHandler>> getHandlers() {
        return List.of(
                CommandHandler.class,
                HelloHandler.class,
                JwtHandler.class,
                SwaggerHandler.class
        );
    }
}