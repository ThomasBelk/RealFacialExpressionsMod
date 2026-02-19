package net.thomasbelk.real_facial_expressions.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

public class RealFacialExpressionCommandCollection extends AbstractCommandCollection {
    public RealFacialExpressionCommandCollection() {
        super("rfe", "Real Facial Expression Commands");
        addSubCommand(new DisplayFaceIdCommand());
        addSubCommand(new ResetFaceIdCommand());
        addSubCommand(new DebugCommandCollection());
    }
}
