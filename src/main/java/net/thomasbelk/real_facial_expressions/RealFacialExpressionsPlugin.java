package net.thomasbelk.real_facial_expressions;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import net.thomasbelk.real_facial_expressions.commands.*;
import net.thomasbelk.real_facial_expressions.components.PlayerFaceAnimationComponent;
import net.thomasbelk.real_facial_expressions.systems.FaceAnimationSystem;
import net.thomasbelk.real_facial_expressions.systems.PlayerJoinLeaveSystem;

public class RealFacialExpressionsPlugin extends JavaPlugin {
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private final Config<RealFacialExpressionsConfig> config = this.withConfig("RealFacialExpressions", RealFacialExpressionsConfig.CODEC);
    private FacePacketReceiver facePacketReceiver;
    private Thread reciverThread;

    public RealFacialExpressionsPlugin(JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Hello from %s version %s", this.getName(), this.getManifest().getVersion().toString());
    }


    @Override
    protected void setup() {
        // config
        config.save(); // ensures the config file is created if it dne.

        // register components
        var entityStoreRegistry = this.getEntityStoreRegistry();
        var faceAnimComponentType = entityStoreRegistry.registerComponent(
                PlayerFaceAnimationComponent.class,
                "RealFacialExpression_PlayerFaceAnimationData",
                PlayerFaceAnimationComponent.CODEC
        );
        PlayerFaceAnimationComponent.setComponentType(faceAnimComponentType);

        // register systems
        entityStoreRegistry.registerSystem(new PlayerJoinLeaveSystem());
        entityStoreRegistry.registerSystem(new FaceAnimationSystem());

        // register commands
        this.getCommandRegistry().registerCommand(new RealFacialExpressionCommandCollection());

        // setup to receive face packets
        facePacketReceiver = new FacePacketReceiver(this.config.get().getPort());
        reciverThread = new Thread(facePacketReceiver, "face-udp-receiver");
        reciverThread.start();
    }

    @Override
    protected void shutdown() {
        if (facePacketReceiver != null) {
            facePacketReceiver.shutdown();
        }
        if (reciverThread != null) {
            try {
                reciverThread.join(); // wait for thread to exit
            } catch (InterruptedException ignored) {}
        }
        super.shutdown();
    }
}
