package net.thomasbelk.real_facial_expressions;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import net.thomasbelk.real_facial_expressions.look.LookCommand;

import java.util.concurrent.ConcurrentLinkedQueue;

public class RealFacialExpressionsPlugin extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    ConcurrentLinkedQueue<FacePacket> faceQueue;
    Thread listenerThread;
    FacePacketListener listener;

    public RealFacialExpressionsPlugin(JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Hello from %s version %s", this.getName(), this.getManifest().getVersion().toString());
    }


    @Override
    protected void setup() {
        this.getCommandRegistry().registerCommand(new LookFrontCommand());
        this.getCommandRegistry().registerCommand(new LookCommand());
    }
}
