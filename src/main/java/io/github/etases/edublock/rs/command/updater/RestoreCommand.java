package io.github.etases.edublock.rs.command.updater;

import io.github.etases.edublock.rs.api.Command;
import io.github.etases.edublock.rs.handler.StudentUpdateHandler;
import org.tinylog.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class RestoreCommand extends Command {
    private final StudentUpdateHandler handler;
    private final AtomicReference<CompletableFuture<Void>> currentFutureRef = new AtomicReference<>();

    public RestoreCommand(StudentUpdateHandler handler) {
        super("updater-restore");
        this.handler = handler;
    }

    @Override
    public void runCommand(String argument) {
        var currentFuture = currentFutureRef.get();
        if (currentFuture != null && !currentFuture.isDone()) {
            Logger.info("Restore already in progress");
        } else {
            currentFutureRef.set(handler.restoreData().thenAccept(v -> Logger.info("Restore complete")));
            Logger.info("Restore started");
        }
    }

    @Override
    public String getDescription() {
        return "Restore the data from the updater to the database";
    }
}
