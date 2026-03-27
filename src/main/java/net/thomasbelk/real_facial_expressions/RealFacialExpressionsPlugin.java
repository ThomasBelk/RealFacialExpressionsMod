package net.thomasbelk.real_facial_expressions;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import net.thomasbelk.real_facial_expressions.commands.*;
import net.thomasbelk.real_facial_expressions.components.PlayerFaceAnimationComponent;
import net.thomasbelk.real_facial_expressions.network.NettyFacePacketReceiver;
import net.thomasbelk.real_facial_expressions.systems.FaceAnimationSystem;
import net.thomasbelk.real_facial_expressions.systems.PlayerJoinLeaveSystem;

public class RealFacialExpressionsPlugin extends JavaPlugin {
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private final Config<RealFacialExpressionsConfig> config = this.withConfig("RealFacialExpressions", RealFacialExpressionsConfig.CODEC);
    private NettyFacePacketReceiver receiver;

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
        receiver = new NettyFacePacketReceiver(this.config.get().getPort());
        safeStartReceiver(receiver);
    }

    public void safeStartReceiver(NettyFacePacketReceiver receiver) {
        try {
            receiver.start();
        } catch (InterruptedException e) {
            LOGGER.atWarning().log("Receiver thread interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
            receiver.shutdown();
        } catch (Exception e) {
            LOGGER.atWarning().log(e.getMessage(), e.getStackTrace());
            receiver.shutdown();
        }
    }

    @Override
    protected void shutdown() {
        receiver.shutdown();
        super.shutdown();
    }
}
