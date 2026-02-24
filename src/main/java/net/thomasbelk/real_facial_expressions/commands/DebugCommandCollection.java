package net.thomasbelk.real_facial_expressions.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

public class DebugCommandCollection extends AbstractCommandCollection {
    public DebugCommandCollection() {
        super("debug", "Debug commands for Real Facial Expressions.");
        addSubCommand(new ViewCommandCollection());
        addSubCommand(new LookCommand());
    }
}
