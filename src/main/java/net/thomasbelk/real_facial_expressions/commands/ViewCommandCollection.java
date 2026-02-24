package net.thomasbelk.real_facial_expressions.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

public class ViewCommandCollection extends AbstractCommandCollection {
    public ViewCommandCollection() {
        super("view", "a set of commands to view front of player and reset to first person");
        addSubCommand(new ViewFrontCommand());
        addSubCommand(new ResetViewCommand());
    }
}
